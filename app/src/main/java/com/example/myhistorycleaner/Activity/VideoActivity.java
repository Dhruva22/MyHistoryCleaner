package com.example.myhistorycleaner.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;

import com.example.myhistorycleaner.Adapter.VideoAdapter;
import com.example.myhistorycleaner.Model.ImagesVideo;
import com.example.myhistorycleaner.R;

import java.util.ArrayList;

import static com.example.myhistorycleaner.Adapter.VideoAdapter.count;
import static com.example.myhistorycleaner.Adapter.VideoAdapter.selected_flag;

public class VideoActivity extends BaseActivity
        implements VideoAdapter.Clickable
{

    private RecyclerView rvVideo;
    Button btnDelete;

    public ArrayList<ImagesVideo> imagesVideoList = new ArrayList<>();
    private VideoAdapter videoAdapter;

    String BucketId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        rvVideo = (RecyclerView)findViewById(R.id.rvVideo);
        btnDelete = (Button)findViewById(R.id.btnDelete);

        Intent intentReceive = getIntent();
        BucketId = intentReceive.getStringExtra("BucketId");

        getAllShownVideoPath(VideoActivity.this);

        videoAdapter = new VideoAdapter(imagesVideoList,VideoActivity.this,
                this,BucketId);

        //GridLayoutManager(getApplicationContext(),2);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2,1);

        rvVideo.setLayoutManager(layoutManager);
        rvVideo.setItemAnimator(new DefaultItemAnimator());
        rvVideo.setAdapter(videoAdapter);


        btnDelete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(VideoActivity.this);
                alertDialogBuilder.setIconAttribute(android.R.attr.alertDialogIcon);
                alertDialogBuilder.setTitle("Alert Dialog");
                alertDialogBuilder.setMessage("Do you want to Delete Video?");
                alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        for(int i = 0; i< imagesVideoList.size(); i++)
                        {
                            ImagesVideo deleteImage = imagesVideoList.get(i);
                            if(deleteImage.isSelected == true)
                            {
                                getContentResolver().delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, MediaStore.Video.Media._ID + " = ? ",
                                        new String[]{deleteImage.getImageId()});
                            }
                            videoAdapter.notifyDataSetChanged();
                        }
                        count = 0;
                        selected_flag = false;
                        imagesVideoList.clear();
                        getAllShownVideoPath(VideoActivity.this);
                    }

                });


                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        count = 0;
                        selected_flag = false;
                        imagesVideoList.clear();
                        getAllShownVideoPath(VideoActivity.this);
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    private void getAllShownVideoPath(Activity activity)
    {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_id,column_index_bucket_name,column_index_bucket_id;

        String absolutePathOfVideo;
        String VideoId;
        String bucketImagesName;
        String bucketImagesId;

        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.Video.Thumbnails.DATA,
                MediaStore.Video.Media._ID,MediaStore.Video.Media.BUCKET_DISPLAY_NAME,MediaStore.Video.Media.BUCKET_ID};

        cursor = activity.getContentResolver().query(uri, projection,"bucket_id=?",
                new String[] { BucketId },
                MediaStore.Video.VideoColumns.DATE_ADDED + " DESC");

        column_index_bucket_id = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_ID);
        column_index_bucket_name = cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);
        column_index_id = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID);

        while (cursor.moveToNext())
        {
            absolutePathOfVideo= cursor.getString(column_index_data);
            VideoId = cursor.getString(column_index_id);
            bucketImagesName = cursor.getString(column_index_bucket_name);
            bucketImagesId = cursor.getString(column_index_bucket_id);

            ImagesVideo imagesVideo = new ImagesVideo(VideoId,absolutePathOfVideo,bucketImagesId,bucketImagesName,false);
            imagesVideoList.add(imagesVideo);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    public void cbClick(int position, Boolean f)
    {
        imagesVideoList.get(position).isSelected = f;
    }

    @Override
    public void onBackPressed()
    {
        Intent folderIntent = new Intent(VideoActivity.this,FolderVideoActivity.class);
        startActivity(folderIntent);
        finish();
    }
}
