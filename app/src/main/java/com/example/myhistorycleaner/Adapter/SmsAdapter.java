package com.example.myhistorycleaner.Adapter;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.myhistorycleaner.Model.SmsModel;
import com.example.myhistorycleaner.R;

import java.util.ArrayList;

/**
 * Created by Mishtiii on 16-01-2017.
 */

public class SmsAdapter extends RecyclerView.Adapter<SmsAdapter.SmsViewHolder>
{
    private ArrayList<SmsModel> smsArrayList = new ArrayList<>();
    Context context;
    public static int count = 0;
    public static boolean selection_flag = false;
    SmsAdapter.Clickable clickable;
    ContentResolver contentResolver;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    public void clearSelections()
    {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    /**
     *
     * @param smsArrayList contains object of type SmsModel received from database
     * @param context contains reference to current activity
     * @param clickable interface that is to be implemented on DisplaySmsActivity
     * @param contentResolver object of ContentResolver
     */
    public SmsAdapter(ArrayList<SmsModel> smsArrayList, Context context,
                      SmsAdapter.Clickable clickable, ContentResolver contentResolver)
    {
        this.smsArrayList = smsArrayList;
        this.context = context;
        this.clickable = clickable;
        this.contentResolver = contentResolver;
    }

    // View holder of sms that binds all the views of item
    public static class SmsViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tvSmsName,tvSmsMsg,tvSmsDate;
        public CheckBox cbSms;
        public CardView cvItemSms;

        public SmsViewHolder(View itemView)
        {
            super(itemView);
            tvSmsName = (TextView) itemView.findViewById(R.id.tvSmsName);
            tvSmsMsg = (TextView) itemView.findViewById(R.id.tvSmsMsg);
            tvSmsDate = (TextView) itemView.findViewById(R.id.tvSmsDate);
            cvItemSms = (CardView)itemView.findViewById(R.id.cvItemSms);
            cbSms = (CheckBox) itemView.findViewById(R.id.cbSms);
        }
    }

    /**
     * interface that contains click method of cardview
     */
    public interface Clickable
    {
        void Smsclick(int position, boolean flag);
    }

    @Override
    public SmsAdapter.SmsViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sms,parent,false);

        SmsAdapter.SmsViewHolder appViewHolder = new SmsAdapter.SmsViewHolder(v);

        return appViewHolder;
    }

    @Override
    public void onBindViewHolder(final SmsAdapter.SmsViewHolder holder,
                                 final int position)
    {
        final SmsModel smsModel = smsArrayList.get(position);
        holder.tvSmsName.setText(smsModel.getName());
        holder.tvSmsMsg.setText(smsModel.getMsg());
        holder.tvSmsDate.setText(smsModel.getDate());

        if(smsModel.isSelected == true)
        {
            holder.cbSms.setVisibility(View.VISIBLE);
            holder.cbSms.setChecked(true);
        }
        else
        {
            holder.cbSms.setVisibility(View.INVISIBLE);
            holder.cbSms.setChecked(false);
        }

        holder.cvItemSms.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v)
            {
                holder.cbSms.setVisibility(View.VISIBLE);
                holder.cbSms.setChecked(true);
                clickable.Smsclick(position, true);
                selectedItems.put(position,true);
                selection_flag = true;
                count++;
                return true;
            }
        });

        holder.cvItemSms.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(selection_flag == true && count > 0 )
                {
                    if(!holder.cbSms.isChecked())
                    {
                            holder.cbSms.setVisibility(View.VISIBLE);
                            holder.cbSms.setChecked(true);
                            selectedItems.put(position,true);
                            count++;
                            clickable.Smsclick(position, true);
                    } else {
                            selectedItems.delete(position);
                            holder.cbSms.setVisibility(View.INVISIBLE);
                            holder.cbSms.setChecked(false);
                            count--;
                            clickable.Smsclick(position, false);
                        }

                }
                else
                {
                    selection_flag = false;
                    count = 0;
                }
            }
        });

        holder.cbSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( ((CheckBox)v).isChecked() )
                {
                    holder.cbSms.setVisibility(View.VISIBLE);
                    holder.cbSms.setChecked(true);
                    count++;
                    clickable.Smsclick(position,true);
                    // perform logic
                    holder.cbSms.refreshDrawableState();
                }
                else
                {
                    holder.cbSms.setVisibility(View.INVISIBLE);
                    holder.cbSms.setChecked(false);
                    count--;
                    clickable.Smsclick(position, false);
                    holder.cbSms.refreshDrawableState();
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return smsArrayList.size();
    }
}
