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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myhistorycleaner.Model.Calls;
import com.example.myhistorycleaner.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mishtiii on 13-01-2017.
 */

public class CallAdapter extends RecyclerView.Adapter<CallAdapter.CallViewHolder>
{
    private ArrayList<Calls> callArrayList = new ArrayList<>();
    Context context;

    public static int count = 0;
    public static boolean selection_flag = false;

    Clickable clickable;
    ContentResolver contentResolver;

    private SparseBooleanArray selectedItems = new SparseBooleanArray();


    public void toggleSelection(int pos)
    {
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        }
        else {
            selectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<Integer>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public void deleteRecyclerData(int position){
        callArrayList.remove(position);
        notifyItemRemoved(position);
    }

    /**
     *
     * @param callArrayList contains object of type Calls received from database
     * @param context contains reference to current activity
     * @param clickable interface that is to be implemented on DisplayCallsActivity
     * @param contentResolver object of ContentResolver
     */
    public CallAdapter(ArrayList<Calls> callArrayList, Context context, Clickable clickable, ContentResolver contentResolver)
    {
        this.callArrayList = callArrayList;
        this.context = context;
        this.clickable = clickable;
        this.contentResolver = contentResolver;
    }

    // View holder of call that binds all the views of item
    public static class CallViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView ivContactImage;
        public TextView tvName,tvNumber,tvType,tvDate,tvDuration;
        public Button btnSelectAll;
        public CheckBox cbContact;
        public CardView cvItemCall;

        public CallViewHolder(View itemView)
        {
            super(itemView);
            ivContactImage = (ImageView) itemView.findViewById(R.id.ivContactImage);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvNumber = (TextView) itemView.findViewById(R.id.tvNumber);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvDuration = (TextView) itemView.findViewById(R.id.tvDuration);
            tvType = (TextView) itemView.findViewById(R.id.tvType);
            cvItemCall = (CardView)itemView.findViewById(R.id.cvItemCall);
            cbContact = (CheckBox) itemView.findViewById(R.id.cbContact);
            btnSelectAll = (Button) itemView.findViewById(R.id.btnSelectAll);
        }


    }

    @Override
    public CallViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_call,parent,false);

        CallViewHolder appViewHolder = new CallViewHolder(v);

        return appViewHolder;
    }

    @Override
    public void onBindViewHolder(final CallViewHolder holder, final int position)
    {
        final Calls calls = callArrayList.get(position);
        holder.tvName.setText(calls.getName());
        holder.tvNumber.setText(calls.getNumber());
        holder.tvType.setText(calls.getType());
        holder.tvDate.setText(calls.getDate());
        holder.tvDuration.setText(calls.getDuration());
        holder.cbContact.setChecked(callArrayList.get(position).isSelected());
        holder.cbContact.setTag(callArrayList.get(position));
        //holder.cvItemCall.setSelected(selectedItems.get(position,false));

        if(calls.isSelected == true)
        {
            holder.cbContact.setVisibility(View.VISIBLE);
            holder.cbContact.setChecked(true);
        }
        else
        {
            holder.cbContact.setVisibility(View.INVISIBLE);
            holder.cbContact.setChecked(false);
        }

        holder.cvItemCall.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v)
            {
                holder.cbContact.setVisibility(View.VISIBLE);
                holder.cbContact.setChecked(true);
                clickable.click(position, true);
                selectedItems.put(position,true);
                selection_flag = true;
                count++;
                return true;
            }
        });

        holder.cvItemCall.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                if(selection_flag == true && count > 0 )
                {
                    if(!holder.cbContact.isChecked())
                    {
                        holder.cbContact.setVisibility(View.VISIBLE);
                        holder.cbContact.setChecked(true);
                        selectedItems.put(position,true);
                        count++;
                        clickable.click(position, true);
                    }
                    else
                    {
                            selectedItems.delete(position);
                            holder.cbContact.setVisibility(View.INVISIBLE);
                            holder.cbContact.setChecked(false);
                            count--;
                            clickable.click(position, false);
                    }
                }
                else
                {
                    selection_flag = false;
                    count = 0;
                    for(int i=0; i<callArrayList.size(); i++)
                    {
                        holder.cbContact.setVisibility(View.INVISIBLE);
                    }

                }
            }
        });

        holder.cbContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( ((CheckBox)v).isChecked() )
                {
                    holder.cbContact.setVisibility(View.VISIBLE);
                    holder.cbContact.setChecked(true);
                    count++;
                    clickable.click(position,true);
                    // perform logic
                    holder.cbContact.refreshDrawableState();
                }
                else
                {
                    holder.cbContact.setVisibility(View.INVISIBLE);
                    holder.cbContact.setChecked(false);
                    count--;
                    clickable.click(position, false);
                    holder.cbContact.refreshDrawableState();
                }
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return callArrayList.size();
    }

    /**
     * interface that contains click method of checkbox
     */
    public interface Clickable
    {
        void click(int position,boolean flag);
    }
}
