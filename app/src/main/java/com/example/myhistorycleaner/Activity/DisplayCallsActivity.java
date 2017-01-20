package com.example.myhistorycleaner.Activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import com.example.myhistorycleaner.Adapter.CallViewPagerAdapter;
import com.example.myhistorycleaner.R;

public class DisplayCallsActivity extends BaseActivity
{
    private Toolbar toolbar;
    private TabLayout tlDisplayCalls;
    private ViewPager vpDisplayCalls;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_calls);

        //toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        vpDisplayCalls = (ViewPager) findViewById(R.id.vpDisplayCalls);
        vpDisplayCalls.setAdapter(new CallViewPagerAdapter(getSupportFragmentManager(),
                DisplayCallsActivity.this));

        tlDisplayCalls = (TabLayout) findViewById(R.id.tlDisplayCalls);
        tlDisplayCalls.setupWithViewPager(vpDisplayCalls);
    }
}
