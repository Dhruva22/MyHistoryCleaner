package com.example.myhistorycleaner.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.myhistorycleaner.Fragments.AllCallsFragment;
import com.example.myhistorycleaner.Fragments.IncomingCallsFragment;
import com.example.myhistorycleaner.Fragments.MissedCallsFragment;
import com.example.myhistorycleaner.Fragments.OutgoingCallsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mishtiii on 13-01-2017.
 */

public class CallViewPagerAdapter extends FragmentPagerAdapter
{
    final int PAGE_COUNT = 4;

    private String tabTitles[] = new String[] { "All", "Received", "Dialed","Missed" };
    private Context context;

    public CallViewPagerAdapter(FragmentManager manager, Context context)
    {
        super(manager);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position)
    {
        if(position == 0)
        {
            return AllCallsFragment.newInstance(position + 1);
        }
        else if(position == 1)
        {
            return IncomingCallsFragment.newInstance(position + 1);
        }
        else if(position == 2)
        {
            return OutgoingCallsFragment.newInstance(position + 1);
        }
        else
        {
            return MissedCallsFragment.newInstance(position + 1);
        }
    }

    @Override
    public int getCount()
    {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return tabTitles[position];
    }
}
