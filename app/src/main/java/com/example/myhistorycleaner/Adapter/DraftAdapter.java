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
 * Created by Mishtiii on 17-01-2017.
 */

public class DraftAdapter extends RecyclerView.Adapter<DraftAdapter.DraftViewHolder>
{
    private ArrayList<SmsModel> draftArrayList = new ArrayList<>();
    Context context;
    public static int count = 0;
    public static boolean selection_flag = false;
    Clickable clickable;
    ContentResolver contentResolver;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    public void clearSelections()
    {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    /**
     *
     * @param draftArrayList contains object of type SmsModel received from database
     * @param context contains reference to current activity
     * @param clickable interface that is to be implemented on DisplaySmsActivity
     * @param contentResolver object of ContentResolver
     */
    public DraftAdapter(ArrayList<SmsModel> draftArrayList, Context context,
                        DraftAdapter.Clickable clickable,
                        ContentResolver contentResolver)
    {
        this.draftArrayList = draftArrayList;
        this.context = context;
        this.clickable = clickable;
        this.contentResolver = contentResolver;
    }

    // View holder of sms that binds all the views of item
    public static class DraftViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tvDraftName,tvDraftMsg;
        public CheckBox cbDraft;
        public CardView cvItemDraft;

        public DraftViewHolder(View itemView)
        {
            super(itemView);
            tvDraftName = (TextView) itemView.findViewById(R.id.tvDraftName);
            tvDraftMsg = (TextView) itemView.findViewById(R.id.tvDraftMsg);
            cvItemDraft = (CardView) itemView.findViewById(R.id.cvItemDraft);
            cbDraft = (CheckBox) itemView.findViewById(R.id.cbDraft);
        }
    }

    @Override
    public DraftAdapter.DraftViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_draft,parent,false);

        DraftAdapter.DraftViewHolder appViewHolder = new DraftAdapter.DraftViewHolder(v);

        return appViewHolder;
    }

    @Override
    public void onBindViewHolder(final DraftAdapter.DraftViewHolder holder,
                                 final int position)
    {
        final SmsModel smsModel = draftArrayList.get(position);
        holder.tvDraftName.setText(smsModel.getName());
        holder.tvDraftMsg.setText(smsModel.getMsg());

        if(smsModel.isSelected == true)
        {
            holder.cbDraft.setVisibility(View.VISIBLE);
            holder.cbDraft.setChecked(true);
        }
        else
        {
            holder.cbDraft.setVisibility(View.INVISIBLE);
            holder.cbDraft.setChecked(false);
        }

        holder.cvItemDraft.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v)
            {
                holder.cbDraft.setVisibility(View.VISIBLE);
                holder.cbDraft.setChecked(true);
                clickable.Draftclick(position, true);
                selectedItems.put(position,true);
                selection_flag = true;
                count++;
                return true;
            }
        });

        holder.cvItemDraft.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(selection_flag == true && count > 0 )
                {
                    if(!holder.cbDraft.isChecked())
                    {
                            holder.cbDraft.setVisibility(View.VISIBLE);
                            holder.cbDraft.setChecked(true);
                            selectedItems.put(position,true);
                            count++;
                            clickable.Draftclick(position, true);
                        } else {

                            holder.cbDraft.setVisibility(View.INVISIBLE);
                            holder.cbDraft.setChecked(false);
                            selectedItems.delete(position);
                            count--;
                            clickable.Draftclick(position, false);
                        }

                }
                else
                {
                    selection_flag = false;
                    count = 0;
                }
            }
        });

        holder.cbDraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( ((CheckBox)v).isChecked() )
                {
                    holder.cbDraft.setVisibility(View.VISIBLE);
                    holder.cbDraft.setChecked(true);
                    count++;
                    clickable.Draftclick(position,true);
                    // perform logic
                    holder.cbDraft.refreshDrawableState();
                }
                else
                {
                    holder.cbDraft.setVisibility(View.INVISIBLE);
                    holder.cbDraft.setChecked(false);
                    count--;
                    clickable.Draftclick(position, false);
                    holder.cbDraft.refreshDrawableState();
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return draftArrayList.size();
    }

    /**
     * interface that contains click method of cardview
     */
    public interface Clickable
    {
        void Draftclick(int position, boolean flag);
    }
}
