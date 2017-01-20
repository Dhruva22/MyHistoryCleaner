package com.example.myhistorycleaner.Fragments;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.Telephony;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.myhistorycleaner.Adapter.SmsAdapter;
import com.example.myhistorycleaner.Model.Calls;
import com.example.myhistorycleaner.Model.SmsModel;
import com.example.myhistorycleaner.R;
import com.example.myhistorycleaner.Utils.Util;

import java.util.ArrayList;

import static com.example.myhistorycleaner.Adapter.SmsAdapter.count;
import static com.example.myhistorycleaner.Adapter.SmsAdapter.selection_flag;
import static com.example.myhistorycleaner.Utils.Constants.SMS_REQUEST;
import static com.example.myhistorycleaner.Utils.Util.formatDate;
import static com.example.myhistorycleaner.Utils.Util.hasPermissions;
import static com.example.myhistorycleaner.Utils.Util.progressBar;
import static com.example.myhistorycleaner.Utils.Util.requestToPermissions;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllSmsFragment extends Fragment implements SmsAdapter.Clickable
{
    View view;

    //Declaration of variables to keep counts of each call type
    public static int allSmsCount = 0;
    public static int inboxCount = 0;
    public static int sentCount = 0;
    public static int draftCount = 0;

    //Array of all the permissions required for accessing sms
    String[] sms_permissions= new String[]
            {
                    Manifest.permission.READ_SMS,
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.RECEIVE_MMS };

    Button btnClear,btnSelectAll;
    CardView cvItemSms;
    RecyclerView rvAllSms;
    SmsAdapter smsAdapter;
    RecyclerView.LayoutManager layoutManager;

    //Array list containing object of calls
    ArrayList<SmsModel> allSmsArrayList = new ArrayList<>();

    public AllSmsFragment()
    {
        // Required empty public constructor
    }

    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    public static AllSmsFragment newInstance(int page)
    {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        AllSmsFragment allSmsFragment = new AllSmsFragment();
        allSmsFragment.setArguments(args);
        return allSmsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_all_sms, container, false);


        final ContentResolver cr = getActivity().getContentResolver();

        btnClear = (Button) view.findViewById(R.id.btnClear);
        btnSelectAll = (Button) view.findViewById(R.id.btnSelectAll);
        cvItemSms = (CardView) view.findViewById(R.id.cvItemSms);
        rvAllSms = (RecyclerView) view.findViewById(R.id.rvAllSms);
        rvAllSms.setHasFixedSize(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            if(!Telephony.Sms.getDefaultSmsPackage(getActivity()
                    .getApplicationContext())
                    .equals(getActivity().getApplicationContext().getPackageName()))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Do you want to make My History Cleaner as default messaging app?")
                        .setCancelable(false)
                        .setTitle("Alert!")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                System.exit(0);
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @TargetApi(25)
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                Intent intent =
                                        new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);

                                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                                        getActivity().getPackageName());

                                startActivity(intent);
                            }
                        });
                builder.show();
            }

        }
        allSmsArrayList.clear();
        getSmsDetails();

        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvAllSms.setLayoutManager(layoutManager);

        smsAdapter = new SmsAdapter(allSmsArrayList,getActivity(),this,cr);
        rvAllSms.setAdapter(smsAdapter);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws SecurityException
            {
                AlertDialog.Builder alertDialogBuilder =
                        new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setIconAttribute(android.R.attr.alertDialogIcon);
                alertDialogBuilder.setTitle("Alert Dialog");
                alertDialogBuilder.setMessage("Do you want to Delete All SMS?");

                alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) throws SecurityException {
                        if (hasPermissions(getActivity(),sms_permissions)) {
                            int countMsg = 0;
                            for (int i = 0; i < allSmsArrayList.size(); i++) {
                                SmsModel msmsModel = allSmsArrayList.get(i);
                                Log.i("id", msmsModel.getId());
                                if (msmsModel.isSelected == true) {
                                    getActivity().getContentResolver().
                                            delete(Uri.parse("content://sms/" + msmsModel.
                                                    getId()), null, null);
                                    countMsg++;
                                    allSmsCount = allSmsCount - 1;
                                }
                            }
                            Toast.makeText(getActivity(), countMsg + " messages deleted",
                                    Toast.LENGTH_LONG).show();
                            count = 0;
                            selection_flag = false;
                            allSmsArrayList.clear();
                            smsAdapter.notifyDataSetChanged();
                            getSmsDetails();
                        }
                    }
                });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        count = 0;
                        selection_flag = false;
                        allSmsArrayList.clear();
                        smsAdapter.notifyDataSetChanged();
                        getSmsDetails();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        btnSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(btnSelectAll.getText().equals("Select All")) {
                    for (int i = 0; i < allSmsArrayList.size(); i++) {
                        allSmsArrayList.get(i).setSelected(true);
                        Smsclick(i, true);
                    }
                    btnSelectAll.setText("Clear All");
                }
                else
                {
                    for (int i = 0; i < allSmsArrayList.size(); i++) {
                        allSmsArrayList.get(i).setSelected(false);
                        Smsclick(i, false);
                    }
                    btnSelectAll.setText("Select All");
                }
                smsAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    private void getSmsDetails()
    {
        if(hasPermissions(getActivity(),sms_permissions))
        {
            progressBar(getActivity(),"Loading.... ");
            Uri message = Uri.parse("content://sms/");
            String strOrder = Telephony.Sms.DEFAULT_SORT_ORDER;

            Cursor c = getActivity().getContentResolver().query(message,null,null,null,strOrder);
            allSmsCount = c.getCount();

            int id = c.getColumnIndex(Telephony.Sms._ID);
            int name = c.getColumnIndex(Telephony.Sms.ADDRESS);
            int msg = c.getColumnIndex(Telephony.Sms.BODY);
            int type = c.getColumnIndex(Telephony.Sms.TYPE);
            int date = c.getColumnIndex(Telephony.Sms.DATE);

            while (c.moveToNext())
            {
                String dir = null;
                int dircode = Integer.parseInt(c.getString(type));
                switch (dircode)
                {
                    case 1:
                    {
                        dir = "INBOX";
                        inboxCount++;
                        break;
                    }
                    case 2:
                    {
                        dir = "SENT";
                        sentCount++;
                        break;
                    }
                    case 3:
                    {
                        dir = "DRAFT";
                        draftCount++;
                        break;
                    }
                }
                String smsDate = c.getString(date);
                String smsDay = formatDate(Long.valueOf(smsDate));
                allSmsArrayList.add(new SmsModel(c.getString(id),c.getString(name),
                        c.getString(msg), dir,
                        smsDay + "", false));
            }
            c.close();
        }
        else
        {
            requestToPermissions(getActivity(),sms_permissions,SMS_REQUEST);
        }
    }

    public void updateList(final SmsModel latestSms)
    {
        allSmsArrayList.add(latestSms);
        smsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case SMS_REQUEST:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    //onClick(btMissedCalls);
                    return;
                }
                else
                {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getActivity(),
                            "Permissions Denied",Toast.LENGTH_LONG).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void Smsclick(int position, boolean flag)
    {
        allSmsArrayList.get(position).isSelected = flag;
    }


}
