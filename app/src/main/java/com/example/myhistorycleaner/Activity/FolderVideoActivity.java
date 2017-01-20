package com.example.myhistorycleaner.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myhistorycleaner.Adapter.FolderVideoAdapter;
import com.example.myhistorycleaner.Model.Folder;
import com.example.myhistorycleaner.R;

import java.util.ArrayList;
import java.util.HashSet;

import static com.example.myhistorycleaner.Adapter.FolderVideoAdapter.count;
import static com.example.myhistorycleaner.Adapter.FolderVideoAdapter.selected_flag;
import static com.example.myhistorycleaner.Utils.Util.hasPermissions;
import static com.example.myhistorycleaner.Utils.Util.requestToPermissions;

public class FolderVideoActivity extends BaseActivity
        implements FolderVideoAdapter.Clickable
{

    public RecyclerView rvVideoFolder;
    Button btnVideoFolder;

    public ArrayList<Folder> folderVideoList = new ArrayList<>();
    ArrayList<Folder> listOfAllVideoFolder;

    private FolderVideoAdapter folderVideoAdapter;

    int position = -1;

    public static final int MY_MULTIPLE_PERMISSION= 100;

    String[] permissions= new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_video);

        rvVideoFolder = (RecyclerView)findViewById(R.id.rvFolderVideo);
        btnVideoFolder = (Button)findViewById(R.id.btnVideoFolder);

        btnVideoFolder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FolderVideoActivity.this);
                alertDialogBuilder.setIconAttribute(android.R.attr.alertDialogIcon);
                alertDialogBuilder.setTitle("Alert Dialog");
                alertDialogBuilder.setMessage("Do you want to Delete Folder?");
                alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        for(int j=0;j<folderVideoList.size();j++)
                        {
                            Folder deleteFolder = folderVideoList.get(j);
                            if(deleteFolder.isSelected==true)
                            {
                                for(int i = 0; i< listOfAllVideoFolder.size(); i++)
                                {
                                    Folder deleteimage = listOfAllVideoFolder.get(i);

                                    if(deleteFolder.getBucketImagesId().equals(deleteimage.getBucketImagesId()))
                                    {
                                        getContentResolver().delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, MediaStore.Video.Media.BUCKET_ID + " = ? ",
                                                new String[]{deleteFolder.getBucketImagesId()});
                                    }

                                }
                            }
                        }
                        count = 0 ;
                        selected_flag = false;
                        folderVideoList.clear();
                        listOfAllVideoFolder.clear();
                        folderVideoAdapter.notifyDataSetChanged();

                        getAllVideoFolder(FolderVideoActivity.this);
                    }

                });


                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        count = 0 ;
                        selected_flag = false;
                        folderVideoList.clear();
                        listOfAllVideoFolder.clear();
                        folderVideoAdapter.notifyDataSetChanged();

                        getAllVideoFolder(FolderVideoActivity.this);
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        if(hasPermissions(FolderVideoActivity.this,permissions))
        {
            getAllVideoFolder(this);
        }
        else
        {
            requestToPermissions(FolderVideoActivity.this,
                    permissions,MY_MULTIPLE_PERMISSION);
        }

        folderVideoAdapter = new FolderVideoAdapter(folderVideoList,this,this);

        //GridLayoutManager(getApplicationContext(),2);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2,1);

        rvVideoFolder.setLayoutManager(layoutManager);
        rvVideoFolder.setItemAnimator(new DefaultItemAnimator());
        rvVideoFolder.setAdapter(folderVideoAdapter);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_MULTIPLE_PERMISSION)
        {
            if (grantResults.length > 0 && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED)
            {
            }
        }
        else
        {
            Toast.makeText(this, "Permissions Denied", Toast.LENGTH_SHORT)
                    .show();
        }

    }
    @Override
    protected void onResume()
    {
        super.onResume();
    }


    private void getAllVideoFolder(Activity activity)
    {
        listOfAllVideoFolder = new ArrayList<>();

        Uri uri;
        Cursor cursor;
        int column_index_bucket_name,column_index_bucket_id,column_index_data;

        String absolutePathOfImage;
        String bucketName;
        String bucketId;

        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.BUCKET_ID,MediaStore.Video.
                Thumbnails.DATA};

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, MediaStore.Video.VideoColumns.DATE_ADDED + " DESC");

        column_index_bucket_id = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_ID);
        column_index_bucket_name = cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME);
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);

        while (cursor.moveToNext())
        {
            absolutePathOfImage = cursor.getString(column_index_data);
            bucketName = cursor.getString(column_index_bucket_name);
            bucketId = cursor.getString(column_index_bucket_id);

            Folder folder = new Folder(bucketId,bucketName,absolutePathOfImage,false);
            listOfAllVideoFolder.add(folder);

        }

        HashSet<String> setbucketname = new HashSet<String>();

        for( Folder f : listOfAllVideoFolder )
        {
            if( setbucketname.add( f.getBucketImagesName() ))
            {
                folderVideoList.add( f );
            }
        }
    }

    @Override
    public void cbClick(int position, String BucketId,Boolean isSelect,Boolean click)
    {
        if(click == false)
        {
            Intent intentGallery = new Intent(this,VideoActivity.class);
            intentGallery.putExtra("BucketId",BucketId);
            startActivity(intentGallery);
            finish();

        }
        else
        {
            folderVideoList.get(position).isSelected = isSelect;
            this.position=position;
        }
    }
}
