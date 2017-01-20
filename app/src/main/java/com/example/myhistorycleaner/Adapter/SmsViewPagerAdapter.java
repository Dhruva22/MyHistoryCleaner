package com.example.myhistorycleaner.Adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.myhistorycleaner.Fragments.AllSmsFragment;
import com.example.myhistorycleaner.Fragments.DraftFragment;
import com.example.myhistorycleaner.Fragments.InboxFragment;
import com.example.myhistorycleaner.Fragments.SentSmsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mishtiii on 16-01-2017.
 */

public class SmsViewPagerAdapter extends FragmentPagerAdapter
{
    final int PAGE_COUNT = 4;
    private String smsTabTitles[] = new String[] { "All", "Inbox", "Sent", "Draft"};
    private Context context;
    public SmsViewPagerAdapter(FragmentManager fm,Context context)
    {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position)
    {
        if(position == 0)
        {
            return AllSmsFragment.newInstance(position + 1);
        }
        else if(position == 1)
        {
            return InboxFragment.newInstance(position + 1);
        }
        else if(position == 2)
        {
            return SentSmsFragment.newInstance(position + 1);
        }
        else
        {
            return DraftFragment.newInstance(position + 1);
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
        return smsTabTitles[position];
    }
}
