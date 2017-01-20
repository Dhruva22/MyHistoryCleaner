package com.example.myhistorycleaner.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myhistorycleaner.Model.Folder;
import com.example.myhistorycleaner.R;

import java.util.ArrayList;

/**
 * Created by Tej710 on 12-01-2017.
 */

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {
    ArrayList<Folder> folderList;
    Context context;
    Clickable clickable;
    public static int count=0;
    public static Boolean selected_flag=false;
    public FolderAdapter(ArrayList<Folder> folderList, Context context, Clickable clickable) {
        this.folderList = folderList;
        this.context = context;
        this.clickable = clickable;
    }

    public interface Clickable{
        void cbClick(int position, String BucketId, Boolean isSelected, Boolean click);

    }


    @Override
    public FolderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_folder_list,parent,false);

        return new FolderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FolderViewHolder holder, final int position) {

            final Folder folder = folderList.get(position);

            if(folder.isSelected == true)
            {
                holder.cbFolder.setVisibility(View.VISIBLE);
                holder.cbFolder.setChecked(true);
                clickable.cbClick(position,folder.bucketImagesId,true,true);
            }
            else {
                holder.cbFolder.setVisibility(View.INVISIBLE);
                holder.cbFolder.setChecked(false);
                clickable.cbClick(position,folder.bucketImagesId,false,true);
            }

            //CardView LongpressclickListener
            holder.cdFolder.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    holder.cbFolder.setVisibility(View.VISIBLE);
                    holder.cbFolder.setChecked(true);

                    count++;
                    selected_flag=true;
                    clickable.cbClick(position,folder.bucketImagesId,true,true);

                    return true;
                }
            });
            holder.cdFolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(selected_flag == true && count > 0)
                    {
                            if(!holder.cbFolder.isChecked())
                            {
                                holder.cbFolder.setVisibility(View.VISIBLE);
                                holder.cbFolder.setChecked(true);
                                count++;
                                clickable.cbClick(position,folder.bucketImagesId,true,true);
                            }
                            else
                            {
                                holder.cbFolder.setVisibility(View.INVISIBLE);
                                holder.cbFolder.setChecked(false);

                                count--;
                                clickable.cbClick(position,folder.bucketImagesId,false,true);
                            }
                    }
                    else
                    {
                        selected_flag = false;
                        count = 0;
                        clickable.cbClick(position,folder.bucketImagesId,false,false);

                    }

                }
            });
            Glide.with(context).load(folder.getAbsolutePathOfImage())
                .fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.ivFolder);


            holder.tvFolder.setText(folder.bucketImagesName);


            holder.cbFolder.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (((CheckBox) v).isChecked())
                    {
                        holder.cdFolder.setBackgroundColor(Color.YELLOW);
                        holder.cbFolder.setVisibility(View.VISIBLE);
                        holder.cbFolder.setChecked(true);

                        count++;

                        clickable.cbClick(position,folder.bucketImagesId, true,true);
                    }
                    else
                    {
                        holder.cbFolder.setVisibility(View.INVISIBLE);
                        holder.cbFolder.setChecked(false);
                        count--;
                        clickable.cbClick(position,folder.bucketImagesId, false,true);
                    }
                }
            });
    }

    @Override
    public int getItemCount() {
        Log.i("Image count",folderList.size()+"");
        return folderList.size();
    }



    public class FolderViewHolder extends RecyclerView.ViewHolder
    {
        ImageView ivFolder;
        TextView tvFolder;
        CheckBox cbFolder;
        CardView cdFolder;
        public FolderViewHolder(View itemView) {
            super(itemView);
            ivFolder = (ImageView) itemView.findViewById(R.id.ivFolder);
            tvFolder = (TextView) itemView.findViewById(R.id.tvFolder);
            cbFolder = (CheckBox)itemView.findViewById(R.id.cbFolder);
            cdFolder = (CardView)itemView.findViewById(R.id.cdFolder);
        }
    }
}