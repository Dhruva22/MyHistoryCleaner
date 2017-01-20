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

import com.example.myhistorycleaner.Adapter.ImageAdapter;
import com.example.myhistorycleaner.Model.ImagesVideo;
import com.example.myhistorycleaner.R;

import java.util.ArrayList;

import static com.example.myhistorycleaner.Adapter.ImageAdapter.count;
import static com.example.myhistorycleaner.Adapter.ImageAdapter.selected_flag;

public class GalleryActivity extends BaseActivity
        implements ImageAdapter.Clickable
{

    private RecyclerView rvGallery;
    Button btnGalleryImages;

    public ArrayList<ImagesVideo> imagesVideoList = new ArrayList<>();
    private ImageAdapter imageAdapter;

    String BucketId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        rvGallery = (RecyclerView)findViewById(R.id.rvGallery);
        btnGalleryImages = (Button)findViewById(R.id.btnGalleryimages);

        Intent intentReceive = getIntent();
        BucketId = intentReceive.getStringExtra("BucketId");

        getAllShownImagesPath(GalleryActivity.this);

        imageAdapter = new ImageAdapter(imagesVideoList,GalleryActivity.this,
                this,BucketId);

        //GridLayoutManager(getApplicationContext(),2);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2,1);

        rvGallery.setLayoutManager(layoutManager);
        rvGallery.setItemAnimator(new DefaultItemAnimator());
        rvGallery.setAdapter(imageAdapter);


        btnGalleryImages.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GalleryActivity.this);
                alertDialogBuilder.setIconAttribute(android.R.attr.alertDialogIcon);
                alertDialogBuilder.setTitle("Alert Dialog");
                alertDialogBuilder.setMessage("Do you want to Delete Image?");
                alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        for(int i = 0; i< imagesVideoList.size(); i++)
                        {
                            ImagesVideo deleteImage = imagesVideoList.get(i);
                            if(deleteImage.isSelected == true)
                            {
                                getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media._ID + " = ? ",
                                        new String[]{deleteImage.getImageId()});
                            }
                            imageAdapter.notifyDataSetChanged();
                        }
                        count = 0;
                        selected_flag = false;
                        imagesVideoList.clear();
                        getAllShownImagesPath(GalleryActivity.this);
                    }

                });


                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        count = 0;
                        selected_flag = false;
                        imagesVideoList.clear();
                        getAllShownImagesPath(GalleryActivity.this);
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    private void getAllShownImagesPath(Activity activity)
    {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_id,column_index_bucket_name,column_index_bucket_id;

        String absolutePathOfImage = null;
        String imageId = null;
        String bucketImagesName;
        String bucketImagesId;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media._ID,MediaStore.Images.Media.BUCKET_DISPLAY_NAME,MediaStore.Images.Media.BUCKET_ID};

        cursor = activity.getContentResolver().query(uri, projection,"bucket_id=?",
                new String[] { BucketId },MediaStore.Images.ImageColumns.DATE_ADDED + " DESC");

        column_index_bucket_id = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID);
        column_index_bucket_name = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        column_index_id = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);

        while (cursor.moveToNext())
        {
            absolutePathOfImage = cursor.getString(column_index_data);
            imageId = cursor.getString(column_index_id);
            bucketImagesName = cursor.getString(column_index_bucket_name);
            bucketImagesId = cursor.getString(column_index_bucket_id);

            ImagesVideo imagesVideo = new ImagesVideo(imageId,absolutePathOfImage,bucketImagesId,bucketImagesName,false);
            imagesVideoList.add(imagesVideo);

        }
    }

    @Override
    public void cbClick(int position, Boolean f)
    {
        imagesVideoList.get(position).isSelected = f;
    }

    @Override
    public void onBackPressed() {
        Intent imageFolderIntent = new Intent(GalleryActivity.this,FolderActivity.class);
        startActivity(imageFolderIntent);
        finish();
    }
}
