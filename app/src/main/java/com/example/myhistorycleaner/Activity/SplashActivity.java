package com.example.myhistorycleaner.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.myhistorycleaner.R;

import static com.example.myhistorycleaner.Utils.Util.hasPermissions;
import static com.example.myhistorycleaner.Utils.Util.requestToPermissions;

public class SplashActivity extends BaseActivity
{
    public static final int MY_MULTIPLE_PERMISSION= 100;

    String[] permissions= new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    CountDownTimer countDownTimer = new CountDownTimer(3500,1000)
    {
        @Override
        public void onTick(long millisUntilFinished)
        {

        }

        @Override
        public void onFinish()
        {
            if(hasPermissions(SplashActivity.this,permissions))
            {
                Intent startIntent = new Intent(SplashActivity.this, HomeScreenActivity.class);
                startActivity(startIntent);
                finish();
            }
            else
            {
               requestToPermissions(SplashActivity.this,permissions,MY_MULTIPLE_PERMISSION);
            }
        }
    };
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_MULTIPLE_PERMISSION)
        {
            if (grantResults.length > 0 && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED)
            {
                Intent startIntent = new Intent(SplashActivity.this, HomeScreenActivity.class);
                startActivity(startIntent);
                finish();
            }
        }
        else
        {
            Toast.makeText(this, "Permissions Denied", Toast.LENGTH_SHORT).show();
            System.exit(0);
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        countDownTimer.start();
    }
}
