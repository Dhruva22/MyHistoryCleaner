package com.example.myhistorycleaner.Fragments;


import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
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

import com.example.myhistorycleaner.Adapter.DraftAdapter;

import com.example.myhistorycleaner.Adapter.SmsAdapter;
import com.example.myhistorycleaner.Model.SmsModel;
import com.example.myhistorycleaner.R;


import java.util.ArrayList;

import static com.example.myhistorycleaner.Adapter.DraftAdapter.count;
import static com.example.myhistorycleaner.Adapter.DraftAdapter.selection_flag;
import static com.example.myhistorycleaner.Utils.Constants.SMS_REQUEST;
import static com.example.myhistorycleaner.Utils.Util.formatDate;
import static com.example.myhistorycleaner.Utils.Util.hasPermissions;

/**
 * A simple {@link Fragment} subclass.
 */
public class DraftFragment extends Fragment implements DraftAdapter.Clickable
{
    View view;

    //Declaration of variables to keep counts of each call type
    public static int allSmsCount = 0;
    public static int draftCount = 0;

    //Array of all the permissions required for accessing sms
    String[] sms_permissions= new String[]
            {
                    Manifest.permission.READ_SMS,
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS,
                    Manifest.permission.RECEIVE_MMS };

    Button btnClear,btnSelectAll;
    CardView cvItemDraft;
    RecyclerView rvAllDraft;
    DraftAdapter draftAdapter;
    RecyclerView.LayoutManager layoutManager;

    //Array list containing object of calls
    ArrayList<SmsModel> draftArrayList = new ArrayList<>();

    public DraftFragment() {
        // Required empty public constructor
    }

    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    public static DraftFragment newInstance(int page)
    {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        DraftFragment draftFragment = new DraftFragment();
        draftFragment.setArguments(args);
        return draftFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_draft, container, false);


        btnClear = (Button) view.findViewById(R.id.btnClear);
        btnSelectAll = (Button) view.findViewById(R.id.btnSelectAll);
        cvItemDraft = (CardView) view.findViewById(R.id.cvItemDraft);
        rvAllDraft = (RecyclerView) view.findViewById(R.id.rvAllDraft);
        rvAllDraft.setHasFixedSize(false);

        draftArrayList.clear();
        getDraftDetails();

        layoutManager = new LinearLayoutManager(getActivity());
        rvAllDraft.setLayoutManager(layoutManager);

        draftAdapter = new DraftAdapter(draftArrayList,getActivity(),this,getActivity().getContentResolver());
        rvAllDraft.setAdapter(draftAdapter);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws SecurityException
            {

                AlertDialog.Builder alertDialogBuilder =
                        new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setIconAttribute(android.R.attr.alertDialogIcon);
                alertDialogBuilder.setTitle("Alert Dialog");
                alertDialogBuilder.setMessage("Do you want to Delete All Draft SMS?");

                alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) throws SecurityException {
                        if (hasPermissions(getActivity(), sms_permissions))
                        {
                            int countMsg = 0;
                            for (int i = 0; i < draftArrayList.size(); i++)
                            {
                                SmsModel msmsModel = draftArrayList.get(i);
                                Log.i("id", msmsModel.getId());
                                if (msmsModel.isSelected == true)
                                {
                                    getActivity().getContentResolver().
                                            delete(Uri.parse("content://sms/" + msmsModel.
                                                    getId()), null, null);
                                    countMsg++;
                                    allSmsCount = allSmsCount - 1;
                                }

                            }
                            Toast.makeText(getActivity(), countMsg + " drafts deleted",
                                    Toast.LENGTH_LONG).show();
                            count = 0;
                            selection_flag = false;
                            draftArrayList.clear();
                            draftAdapter.notifyDataSetChanged();
                            getDraftDetails();
                        }
                    }
                });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        SmsAdapter.count = 0;
                        SmsAdapter.selection_flag = false;
                        draftArrayList.clear();
                        draftAdapter.notifyDataSetChanged();
                        getDraftDetails();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        btnSelectAll.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(btnSelectAll.getText().equals("Select All")) {
                    for (int i = 0; i < draftArrayList.size(); i++) {
                        draftArrayList.get(i).setSelected(true);
                        Draftclick(i, true);
                    }
                    btnSelectAll.setText("Clear All");
                }
                else
                {
                    for (int i = 0; i < draftArrayList.size(); i++) {
                        draftArrayList.get(i).setSelected(false);
                        Draftclick(i, false);
                    }
                    btnSelectAll.setText("Select All");
                }
            }
        });

        return view;
    }

    private void getDraftDetails()
    {
        if(hasPermissions(getActivity(), sms_permissions))
        {
            String strOrder = Telephony.Sms.DEFAULT_SORT_ORDER;
            Uri inboxUri = Uri.parse("content://sms/draft/");
            Cursor c = getActivity().getContentResolver().query(inboxUri , null, null, null, strOrder);

            Log.i("size",c.getCount()+"");
            int id = c.getColumnIndex(Telephony.Sms.Draft._ID);
            int name = c.getColumnIndex(Telephony.Sms.Draft.ADDRESS);
            int msg = c.getColumnIndex(Telephony.Sms.Draft.BODY);
            int type = c.getColumnIndex(Telephony.Sms.Draft.TYPE);
            int date = c.getColumnIndex(Telephony.Sms.Draft.DATE);

            while (c.moveToNext())
            {
                String dir = null;
                int dircode = Integer.parseInt(c.getString(type));
                if(dircode == Telephony.Sms.Draft.MESSAGE_TYPE_DRAFT)
                {
                    dir = "Draft";
                    draftCount++;
                }
                String smsDate = c.getString(date);
                String smsDay = formatDate(Long.valueOf(smsDate));
                draftArrayList.add(new SmsModel(c.getString(id),c.getString(name),
                        c.getString(msg), dir,
                        smsDay + "", false));
            }
            c.close();
        }
        allSmsCount = draftArrayList.size();
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
                            "Permissions not Granted",Toast.LENGTH_LONG).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @Override
    public void Draftclick(int position, boolean flag)
    {
        draftArrayList.get(position).isSelected = flag;
    }


}
