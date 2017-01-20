package com.example.myhistorycleaner.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myhistorycleaner.R;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.text.DecimalFormat;

import static com.example.myhistorycleaner.Utils.Util.hasPermissions;
import static com.example.myhistorycleaner.Utils.Util.requestToPermissions;


public class ClearCacheActivity extends BaseActivity {
    Button btnClearCache,btnClearClipboard;
    TextView tvCacheSize;

    public static long size;
    public static long count=-1;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    public static final int MY_MULTIPLE_PERMISSION= 100;
    String[] permissions= new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    File rootFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Android/data");
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear_cache);
        btnClearCache = (Button) findViewById(R.id.btnClearCache);
        btnClearClipboard = (Button)findViewById(R.id.btnClearClipboard);
        tvCacheSize = (TextView)findViewById(R.id.tvCacheSize);
        if(hasPermissions(ClearCacheActivity.this,permissions))
        {
            countApplicationData(rootFile);
        }
        else
        {
            requestToPermissions(ClearCacheActivity.this,permissions,MY_MULTIPLE_PERMISSION);
        }




        btnClearCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if(hasPermissions(ClearCacheActivity.this,permissions)) {

                        Intent i = new Intent(ClearCacheActivity.this,Clear.class);
                        startActivity(i);
                        finish();
                        Log.i("rootfile", rootFile.toString());
                        clearApplicationData(rootFile);

                      //  Toast.makeText(ClearCacheActivity.this, "Clear Cache Data", Toast.LENGTH_SHORT).show();
                    }else {
                        requestToPermissions(ClearCacheActivity.this,permissions,MY_MULTIPLE_PERMISSION);
                    }

                }

        });

        btnClearClipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("","");
                clipboard.setPrimaryClip(clip);
                Toast.makeText(ClearCacheActivity.this,"Clear ClipBoard Data",Toast.LENGTH_SHORT).show();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    //clear all cache files
    public void clearApplicationData(File rootfile) {

        if (rootfile != null) {
            String[] chlidren = rootfile.list();
            for(String child : chlidren)
            {
                File subdirectory_delete = new File(rootfile+"/"+child+"/cache");
                Log.i("pathdir",subdirectory_delete.toString());
                deleteDir(subdirectory_delete);
            }

        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        countApplicationData(rootFile);

    }
    //count All cache file
    public void countApplicationData(File rootfile) {
        size = 0;
        count = 0;
        if (rootfile != null) {
            String[] chlidren = rootfile.list();
            for(String child : chlidren)
            {
                File subdirectory = new File(rootfile+"/"+child+"/cache");
                Log.i("pathdir",subdirectory.toString());

               folderSize(subdirectory);
            }
            String s = getReadableSize(size);
            tvCacheSize.setText("Size :"+s.toString()+"  Count :"+ count);
            Log.i("size",s);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MY_MULTIPLE_PERMISSION: {
                Log.i("tejas", requestCode + "" + resultCode);
                if (requestCode == MY_MULTIPLE_PERMISSION && resultCode == RESULT_OK) {
                    try {
                       countApplicationData(rootFile);
                        tvCacheSize.setText("Size :"+size+"  Count :"+ count);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_MULTIPLE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                countApplicationData(rootFile);
            }
        } else {
            Toast.makeText(this, "permission is denied", Toast.LENGTH_SHORT).show();
        }

    }
    //size formation
    public static String getReadableSize(long size) {
        if(size <= 0) return "0 B";
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/Math.pow(1024, digitGroups))
                + " " + units[digitGroups];
    }
    //count files and size
    public boolean folderSize (File dir)
    {
        File rootdir = dir;
        Log.i("Rootdir",rootdir.toString());
        if (dir != null && dir.isDirectory()) {

            if (dir.listFiles() != null) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    boolean success = folderSize(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }

            }
            return true;
        } else if (dir != null && dir.isFile()) {
            size += dir.length();
            count++;
            return true;
        } else {
            return false;
        }
    }
    //delete dirctory
    public static boolean deleteDir(File dir) throws NullPointerException {

        File rootdir = dir;
        Log.i("Rootdir",rootdir.toString());
        if (dir != null && dir.isDirectory()) {

            if (dir.listFiles() != null) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
                dir.delete();
            }
            return true;
        } else if (dir != null && dir.isFile()) {

            return dir.delete();
        } else {
            return false;
        }

    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("ClearCache Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}

