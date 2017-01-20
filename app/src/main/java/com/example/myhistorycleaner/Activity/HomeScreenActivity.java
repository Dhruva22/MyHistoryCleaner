package com.example.myhistorycleaner.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myhistorycleaner.R;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.text.DecimalFormat;

import static com.example.myhistorycleaner.Utils.Constants.MY_MULTIPLE_PERMISSION;
import static com.example.myhistorycleaner.Utils.Constants.MY_PERMISSIONS_REQUEST;
import static com.example.myhistorycleaner.Utils.Constants.SMS_REQUEST;
import static com.example.myhistorycleaner.Utils.Util.hasPermissions;
import static com.example.myhistorycleaner.Utils.Util.requestToPermissions;

public class HomeScreenActivity extends BaseActivity implements View.OnClickListener
{
    ImageButton ibBoost;
    TextView tvClearedCache,tvMemoryBoost;
    Button btnCallLog,btnSms,btnGallery,btnVideo;

    //Array of all the permissions required for accessing call log
    String[] permissions= new String[]{
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG,
            Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.RECEIVE_MMS,
           };

    String[] call_permissions= new String[]{
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.WRITE_CALL_LOG};

    String[] sms_permissions= new String[]{
            Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.RECEIVE_MMS };

    String[] storage_permissions= new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static long size;
    public static long count=-1;

    private GoogleApiClient client;

    File rootFile = new File(Environment.getExternalStorageDirectory()
            .getAbsolutePath()+"/Android/data");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        ibBoost = (ImageButton) findViewById(R.id.ibBoost);

        tvClearedCache = (TextView) findViewById(R.id.tvClearedCache);
        tvMemoryBoost = (TextView) findViewById(R.id.tvMemoryBoost);

        btnCallLog = (Button) findViewById(R.id.btnCallLog);
        btnSms = (Button) findViewById(R.id.btnSms);
        btnGallery = (Button) findViewById(R.id.btnGallery);
        btnVideo = (Button) findViewById(R.id.btnVideo);

        ibBoost.setOnClickListener(this);

        btnCallLog.setOnClickListener(this);
        btnSms.setOnClickListener(this);
        btnGallery.setOnClickListener(this);
        btnVideo.setOnClickListener(this);

        if(hasPermissions(this,permissions))
        {
            countApplicationData(rootFile);
        }
        else
        {
            requestToPermissions(HomeScreenActivity.this,permissions,MY_MULTIPLE_PERMISSION);
        }

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    //size formation
    public static String getReadableSize(long size)
    {
        if(size <= 0)
        {
            return "0 B";
        }

        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };

        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));

        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups))
                + " " + units[digitGroups];
    }

    //count files and size
    public boolean folderSize (File dir)
    {
        File rootdir = dir;

        if (dir != null && dir.isDirectory())
        {

            if (dir.listFiles() != null)
            {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++)
                {
                    boolean success = folderSize(new File(dir, children[i]));
                    if (!success)
                    {
                        return false;
                    }
                }

            }
            return true;
        }
        else if (dir != null && dir.isFile())
        {
            size += dir.length();
            count++;
            return true;
        }
        else
        {
            return false;
        }
    }


    //delete dirctory
    public static boolean deleteDir(File dir) throws NullPointerException
    {

        File rootdir = dir;

        if (dir != null && dir.isDirectory())
        {

            if (dir.listFiles() != null)
            {
                String[] children = dir.list();

                for (int i = 0; i < children.length; i++)
                {
                    boolean success = deleteDir(new File(dir, children[i]));

                    if (!success)
                    {
                        return false;
                    }
                }

                dir.delete();
            }

            return true;
        }
        else if (dir != null && dir.isFile())
        {
            return dir.delete();
        }
        else
        {
            return false;
        }

    }

    //clear all cache files
    public void clearApplicationData(File rootfile)
    {

        if (rootfile != null)
        {
            String[] chlidren = rootfile.list();
            for(String child : chlidren)
            {
                File subdirectory_delete = new File(rootfile+"/"+child+"/cache");
                deleteDir(subdirectory_delete);
            }

        }
    }

    //count All cache file
    public void countApplicationData(File rootfile)
    {
        size = 0;
        count = 0;
        if (rootfile != null)
        {
            String[] chlidren = rootfile.list();
            for(String child : chlidren)
            {
                File subdirectory = new File(rootfile+"/"+child+"/cache");

                folderSize(subdirectory);
            }

            String s = getReadableSize(size);

            tvClearedCache.setText("Cleared Cache   " + count + " files");
            tvMemoryBoost.setText("Memory Boost   " + s.toString());
        }
    }

    public Action getIndexApiAction()
    {
        Thing object = new Thing.Builder()
                .setName("ClearCache Page")
                // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();

        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop()
    {
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        countApplicationData(rootFile);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case MY_MULTIPLE_PERMISSION:
            {
                if (requestCode == MY_MULTIPLE_PERMISSION && resultCode == RESULT_OK)
                {
                    try
                    {
                        countApplicationData(rootFile);

                        tvMemoryBoost.setText("Memory Boost   " + size);
                        tvClearedCache.setText("Cleared Cache   " + count + " files");
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                }
                break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_MULTIPLE_PERMISSION)
        {
            if(grantResults.length > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED)
            {
            }
        }
        else
        {
            Toast.makeText(this, "Permissions Denied", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnCallLog:
            {
                if(hasPermissions(this,call_permissions))
                {
                    Intent callIntent = new Intent(HomeScreenActivity.this,DisplayCallsActivity.class);
                    startActivity(callIntent);
                }
                else
                {
                    requestToPermissions(HomeScreenActivity.this,call_permissions,MY_PERMISSIONS_REQUEST);
                }

                break;
            }
            case R.id.btnSms:
            {
                if(hasPermissions(this,sms_permissions))
                {
                    Intent smsIntent = new Intent(HomeScreenActivity.this,DisplaySmsActivity.class);
                    startActivity(smsIntent);
                }
                else
                {
                    requestToPermissions(HomeScreenActivity.this,sms_permissions,SMS_REQUEST);
                }
                break;
            }
            case R.id.btnGallery:
            {
                if(hasPermissions(this,storage_permissions))
                {
                    Intent galleryIntent = new Intent(HomeScreenActivity.this,FolderActivity.class);
                    startActivity(galleryIntent);
                }
                else
                {
                    requestToPermissions(HomeScreenActivity.this,storage_permissions,MY_MULTIPLE_PERMISSION);
                }
                break;
            }
            case R.id.btnVideo:
            {
                if(hasPermissions(this,storage_permissions))
                {
                    Intent videoIntent = new Intent(HomeScreenActivity.this,FolderVideoActivity.class);
                    startActivity(videoIntent);
                }
                else
                {
                    requestToPermissions(HomeScreenActivity.this,storage_permissions,MY_MULTIPLE_PERMISSION);
                }

                break;
            }

            case R.id.ibBoost:
            {
                if(hasPermissions(this,storage_permissions))
                {
                    Intent cacheIntent = new Intent(HomeScreenActivity.this,Clear.class);
                    startActivity(cacheIntent);
                    finish();

                    clearApplicationData(rootFile);

                        //  Toast.makeText(ClearCacheActivity.this, "Clear Cache Data", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    requestToPermissions(HomeScreenActivity.this,storage_permissions,MY_MULTIPLE_PERMISSION);
                }
                break;
            }
        }

    }
}
