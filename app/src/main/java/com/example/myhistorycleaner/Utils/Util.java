package com.example.myhistorycleaner.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.myhistorycleaner.Utils.Constants.MY_MULTIPLE_PERMISSION;
import static com.example.myhistorycleaner.Utils.Constants.MY_PERMISSIONS_REQUEST;

/**
 * Created by Mishtiii on 13-01-2017.
 */

public class Util
{
    private static ProgressDialog progress;

    /*public static boolean checkPermission(Context context, int requestcode, String[] permissions)
    {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String p : permissions) {
                result = ContextCompat.checkSelfPermission(context, p);
                if (result != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(p);
                }
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions((Activity) context,
                        listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                        MY_PERMISSIONS_REQUEST);
                return false;
            }
        }
        return true;
    }*/

    public static boolean hasPermissions(Context context, String[] permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void requestToPermissions(Context contexts,String[] permissions,int requestCode)
    {
        if (!hasPermissions(contexts, permissions)) {
            ActivityCompat.requestPermissions((Activity)contexts, permissions, requestCode);
        }
    }

    public static void progressBar(Context context,String message)
    {
        progress = new ProgressDialog(context);
        progress.setMessage(message);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.setMax(10);
        progress.setProgress(0);
        progress.show();

        final int totalProgressTime = 50;
        final Thread t = new Thread()
        {
            @Override
            public void run()
            {
                int jumpTime = 0;

                while(jumpTime < totalProgressTime)
                {
                    try
                    {
                        sleep(20);
                        jumpTime += 5;
                        progress.setProgress(jumpTime);
                    }
                    catch (InterruptedException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                progress.dismiss();
            }
        };
        t.start();
    }

    public static void sendFeedback(Context context, String s) {
        Intent intent = new Intent("android.intent.action.SENDTO");
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra("android.intent.extra.SUBJECT", s + " Feedback");
        try {
            context.startActivity(intent);
        } catch (Exception e) {
        }
    }

    public static int getSDKVersionNumber() {
        try {
            return Integer.valueOf(Build.VERSION.SDK_INT).intValue();
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static int getAndroidVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static String formatDate(long l) {
        return new SimpleDateFormat("MMM dd").format(new Date(l));
    }
}
