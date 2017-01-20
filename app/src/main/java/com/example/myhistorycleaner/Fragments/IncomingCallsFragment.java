package com.example.myhistorycleaner.Fragments;


import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
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
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.myhistorycleaner.Adapter.CallAdapter;
import com.example.myhistorycleaner.Model.Calls;
import com.example.myhistorycleaner.R;
import com.example.myhistorycleaner.Utils.Util;

import java.util.ArrayList;

import static com.example.myhistorycleaner.Adapter.CallAdapter.count;
import static com.example.myhistorycleaner.Adapter.CallAdapter.selection_flag;
import static com.example.myhistorycleaner.Utils.Constants.MY_PERMISSIONS_REQUEST;
import static com.example.myhistorycleaner.Utils.Util.formatDate;
import static com.example.myhistorycleaner.Utils.Util.hasPermissions;

/**
 * A simple {@link Fragment} subclass.
 */
public class IncomingCallsFragment extends Fragment implements CallAdapter.Clickable
{
    View view;

    //Declaration of variables to keep counts of incoming call type
    public static int allCount = 0;
    public static int incomingCount = 0;

    //Array of all the permissions required for accessing call log
    String[] call_permissions= new String[]{
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG};

    Button btnClear,btnSelectAll;
    CardView cvItemCall;
    RecyclerView rvAllCalls;
    CallAdapter callAdapter;
    RecyclerView.LayoutManager layoutManager;

    //Array list containing object of all incoming calls
    ArrayList<Calls> incomingCallArrayList = new ArrayList<>();

    public IncomingCallsFragment()
    {
        // Required empty public constructor
    }

    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    public static IncomingCallsFragment newInstance(int page)
    {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        IncomingCallsFragment fragment = new IncomingCallsFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_all_calls, container, false);


        final ContentResolver cr = getActivity().getContentResolver();

        btnClear = (Button) view.findViewById(R.id.btnClear);
        btnSelectAll = (Button) view.findViewById(R.id.btnSelectAll);
        cvItemCall = (CardView) view.findViewById(R.id.cvItemCall);
        rvAllCalls = (RecyclerView) view.findViewById(R.id.rvAllCalls);
        rvAllCalls.setHasFixedSize(false);

        incomingCallArrayList.clear();
        getIncomingCallDetails();

        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        rvAllCalls.setLayoutManager(layoutManager);

        callAdapter = new CallAdapter(incomingCallArrayList,getActivity(),this,cr);
        rvAllCalls.setAdapter(callAdapter);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) throws SecurityException
            {
                AlertDialog.Builder alertDialogBuilder =
                        new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setIconAttribute(android.R.attr.alertDialogIcon);
                alertDialogBuilder.setTitle("Alert Dialog");
                alertDialogBuilder.setMessage("Do you want to Delete Incoming Call History?");

                alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) throws SecurityException {
                        if (hasPermissions(getActivity(), call_permissions))
                        {
                            int countMsg = 0;
                            for (int i = 0; i < incomingCallArrayList.size(); i++)
                            {
                                Calls mCalls = incomingCallArrayList.get(i);
                                Log.i("id" , mCalls.getId());
                                if (mCalls.isSelected == true) {
                                    getActivity().getContentResolver().
                                            delete(CallLog.Calls.CONTENT_URI, CallLog.Calls._ID + " = ? ",
                                                    new String[]{mCalls.getId()});
                                    countMsg++;
                                    allCount = allCount - 1;
                                }
                            }
                            Toast.makeText(getActivity(),countMsg + " incomming calls deleted",
                                    Toast.LENGTH_LONG).show();
                            count = 0;
                            selection_flag = false;
                            incomingCallArrayList.clear();
                            callAdapter.notifyDataSetChanged();
                            getIncomingCallDetails();
                        }
                    }
                });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        count = 0;
                        selection_flag = false;
                        incomingCallArrayList.clear();
                        callAdapter.notifyDataSetChanged();
                        getIncomingCallDetails();
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
                    for (int i = 0; i < incomingCallArrayList.size(); i++) {
                        incomingCallArrayList.get(i).setSelected(true);
                        click(i, true);
                    }
                    btnSelectAll.setText("Clear All");
                }
                else
                {
                    for (int i = 0; i < incomingCallArrayList.size(); i++) {
                        incomingCallArrayList.get(i).setSelected(false);
                        click(i, false);
                    }
                    btnSelectAll.setText("Select All");
                }
                callAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    private void getIncomingCallDetails() throws SecurityException
    {
        if(hasPermissions(getActivity(), call_permissions))
        {
            String strOrder = android.provider.CallLog.Calls.DATE + " DESC";
            Cursor managedCursor = getActivity().getContentResolver().query(CallLog.Calls.CONTENT_URI, null,"type = 1", null,strOrder);
            int id = managedCursor.getColumnIndex(CallLog.Calls._ID);
            int name = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
            int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
            int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
            int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
            int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);

            while (managedCursor.moveToNext()) {
                String dir = null;
                int dircode = Integer.parseInt(managedCursor.getString(type));

                if(dircode == CallLog.Calls.INCOMING_TYPE)
                {
                        dir = "Received Call";
                        incomingCount++;
                }

                int dur = Integer.parseInt(managedCursor.getString(duration));
                String hr = "",min = "",sec = "",time = "";
                if(dur/3600 > 0)
                {
                    hr = String.valueOf(dur/3600) + "hrs.";
                    dur = dur % 3600;
                }
                if(dur/60 > 0)
                {
                    if(dur/60 < 10)
                    {
                        min = "0" + String.valueOf(dur/60) + "mins.";
                        dur = dur % 60;
                    }
                    else
                    {
                        min = String.valueOf(dur/60) + "mins.";
                        dur = dur % 60;
                    }
                }
                if(dur > 0)
                {
                    if(dur < 10)
                    {
                        sec = "0" + String.valueOf(dur) + "secs.";
                    }
                    else
                    {
                        sec = String.valueOf(dur) + "secs.";
                    }
                }

                if(hr != "")
                {
                    time += hr;
                }
                if (min != "")
                {
                    time += min;
                }
                time += sec;

                String callDate = managedCursor.getString(date);
                String callDayTime = formatDate(Long.valueOf(callDate));

                incomingCallArrayList.add(new Calls(managedCursor.getString(id),managedCursor.getString(name),
                        managedCursor.getString(number), dir,
                        callDayTime + "", time,false));
            }
            managedCursor.close();
        }

        allCount = incomingCallArrayList.size();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST:
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
    public void click(int position, boolean flag) {
        incomingCallArrayList.get(position).isSelected = flag;
    }


}
