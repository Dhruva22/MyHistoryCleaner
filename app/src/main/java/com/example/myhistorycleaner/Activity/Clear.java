package com.example.myhistorycleaner.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myhistorycleaner.R;



public class Clear extends BaseActivity
{
    //Time to launch the another activity
    private static int TIME_OUT = 1500;

    Button btnDone;
    TextView tvCleanApp;
    ImageView ivRotateFan;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear);

        this.getSupportActionBar().hide();

        btnDone = (Button)findViewById(R.id.btnDone);

        tvCleanApp = (TextView) findViewById(R.id.tvCleanApp);
        ivRotateFan =(ImageView) findViewById(R.id.ivRotateFan);

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(125); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(10);

        tvCleanApp.startAnimation(anim);

        RotateAnimation rotate3 = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,  Animation.RELATIVE_TO_SELF, 0.5f);
        rotate3.setInterpolator(new LinearInterpolator());
        rotate3.setDuration(250);
        rotate3.setRepeatCount(5);
        ivRotateFan.startAnimation(rotate3);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
            //    Toast.makeText(Clear.this,"Clear All Cache!!!",Toast.LENGTH_SHORT).show();
                btnDone.setVisibility(View.VISIBLE);
                       /* Intent i = new Intent(ImageRotateActivity.this, MainActivity.class);
                        startActivity(i);
                        finish();*/
            }
        }, TIME_OUT);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent homeIntent = new Intent(Clear.this,HomeScreenActivity.class);
                startActivity(homeIntent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(Clear.this,HomeScreenActivity.class);
        startActivity(i);
        finish();
    }
}
