package com.example.myhistorycleaner.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myhistorycleaner.Model.ImagesVideo;
import com.example.myhistorycleaner.R;

import java.util.ArrayList;

/**
 * Created by Tej710 on 11-01-2017.
 */

public class ImageAdapter extends  RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    ArrayList<ImagesVideo> imagesVideoList;
    Context context;
    Clickable clickable;
    String BucketId;
    public static int count=0;
    public static Boolean selected_flag=false;
    public ImageAdapter(ArrayList<ImagesVideo> imagesVideoList, Context context, Clickable clickable, String BucketId) {
        this.imagesVideoList = imagesVideoList;
        this.context = context;
        this.clickable = clickable;
        this.BucketId = BucketId;
    }

    public interface Clickable{
        void cbClick(int position, Boolean f);

    }


    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery_list,parent,false);

        return new ImageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, final int position) {

        final ImagesVideo imagesVideo = imagesVideoList.get(position);

        if(imagesVideo.isSelected == true)
        {
            holder.cbImage.setVisibility(View.VISIBLE);
            holder.cbImage.setChecked(true);

            clickable.cbClick(position,true);
        }
        else {
            holder.cbImage.setVisibility(View.INVISIBLE);

            holder.cbImage.setChecked(false);

            clickable.cbClick(position,false);
        }
        holder.cdImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                holder.cbImage.setVisibility(View.VISIBLE);
                holder.cbImage.setChecked(true);

                count++;
                selected_flag=true;
                clickable.cbClick(position,true);

                return true;
            }
        });
        holder.cdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selected_flag == true && count > 0)
                {  if(!holder.cbImage.isChecked())
                        {
                            holder.cbImage.setVisibility(View.VISIBLE);
                            holder.cbImage.setChecked(true);
                           count++;
                            clickable.cbClick(position,true);
                        }
                        else
                        {
                            holder.cbImage.setVisibility(View.INVISIBLE);

                            holder.cbImage.setChecked(false);

                            count--;
                            clickable.cbClick(position,false);
                        }


                }
                else
                {
                    selected_flag = false;
                    count = 0;
                }

            }
        });
            Glide.with(context).load(imagesVideo.getImagePath())
                    .fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.ivImage);
            holder.cbImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((CheckBox) v).isChecked()) {

                        holder.cbImage.setVisibility(View.VISIBLE);
                        holder.cbImage.setChecked(true);

                        count++;
                        clickable.cbClick(position, true);
                    } else {
                        holder.cbImage.setVisibility(View.INVISIBLE);

                        holder.cbImage.setChecked(false);

                        count--;
                        clickable.cbClick(position, false);
                    }
                }
            });

    }

    @Override
    public int getItemCount() {
        return imagesVideoList.size();
    }



    public class ImageViewHolder extends RecyclerView.ViewHolder{
        ImageView ivImage;
        CheckBox cbImage;
        CardView cdImage;
        public ImageViewHolder(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            cbImage = (CheckBox) itemView.findViewById(R.id.cbImage);
            cdImage = (CardView) itemView.findViewById(R.id.cdImage);
        }
    }
}