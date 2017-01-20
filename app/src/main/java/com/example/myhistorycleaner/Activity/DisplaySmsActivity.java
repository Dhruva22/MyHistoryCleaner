package com.example.myhistorycleaner.Activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.myhistorycleaner.Adapter.SmsViewPagerAdapter;
import com.example.myhistorycleaner.R;

public class DisplaySmsActivity extends BaseActivity
{
    private Toolbar toolbarSms;
    private TabLayout tlDisplaySms;
    private ViewPager vpDisplaySms;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_sms);

        //toolbarSms = (Toolbar) findViewById(R.id.toolbarSms);
        //setSupportActionBar(toolbarSms);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        vpDisplaySms = (ViewPager) findViewById(R.id.vpDisplaySms);
        vpDisplaySms.setAdapter(new SmsViewPagerAdapter(getSupportFragmentManager(),
                DisplaySmsActivity.this));

        tlDisplaySms = (TabLayout) findViewById(R.id.tlDisplaySms);
        tlDisplaySms.setupWithViewPager(vpDisplaySms);
    }
}
