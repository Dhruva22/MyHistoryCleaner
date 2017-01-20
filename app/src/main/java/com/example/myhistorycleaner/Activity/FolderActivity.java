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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myhistorycleaner.Adapter.FolderAdapter;
import com.example.myhistorycleaner.Model.Folder;
import com.example.myhistorycleaner.R;

import java.util.ArrayList;
import java.util.HashSet;

import static com.example.myhistorycleaner.Utils.Util.hasPermissions;
import static com.example.myhistorycleaner.Utils.Util.requestToPermissions;
import static com.example.myhistorycleaner.Adapter.FolderAdapter.count;
import static com.example.myhistorycleaner.Adapter.FolderAdapter.selected_flag;

public class FolderActivity extends BaseActivity
        implements FolderAdapter.Clickable
{
    private RecyclerView rvFolder;
    ArrayList<Folder> listOfAllFolder;
    public ArrayList<Folder> folderList = new ArrayList<>();

    Button btnGalleryFolder;

    int position = -1;

    private FolderAdapter folderAdapter;
    public static final int MY_MULTIPLE_PERMISSION= 100;

    String[] permissions= new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        btnGalleryFolder = (Button)findViewById(R.id.btnGalleryFolder);

        btnGalleryFolder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(FolderActivity.this);
                alertDialogBuilder.setIconAttribute(android.R.attr.alertDialogIcon);
                alertDialogBuilder.setTitle("Alert Dialog");
                alertDialogBuilder.setMessage("Do you want to Delete Folder?");
                alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        for(int j=0;j<folderList.size();j++)
                        {
                            Folder deleteFolder = folderList.get(j);
                            if(deleteFolder.isSelected==true)
                            {
                                for(int i = 0; i< listOfAllFolder.size(); i++)
                                {
                                    Folder deleteimage = listOfAllFolder.get(i);

                                    if(deleteFolder.getBucketImagesId().equals(deleteimage.getBucketImagesId()))
                                    {
                                        getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Images.Media.BUCKET_ID + " = ? ",
                                                new String[]{deleteFolder.getBucketImagesId()});
                                    }

                                }
                            }
                        }
                        FolderAdapter.count = 0;
                        FolderAdapter.selected_flag= false;
                        folderList.clear();
                        listOfAllFolder.clear();
                        folderAdapter.notifyDataSetChanged();

                        getAllFolder(FolderActivity.this);
                    }

                });


                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FolderAdapter.count = 0;
                        FolderAdapter.selected_flag= false;
                        folderList.clear();
                        listOfAllFolder.clear();
                        folderAdapter.notifyDataSetChanged();

                        getAllFolder(FolderActivity.this);
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                            }
        });

       if (hasPermissions(FolderActivity.this,permissions))
       {
           getAllFolder(this);

           rvFolder = (RecyclerView) findViewById(R.id.rvGalleryFloder);
           folderAdapter = new FolderAdapter(folderList, this, this);

           //GridLayoutManager(getApplicationContext(),2);
           RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, 1);

           rvFolder.setLayoutManager(layoutManager);
           rvFolder.setItemAnimator(new DefaultItemAnimator());
           rvFolder.setAdapter(folderAdapter);

       }
       else
       {
           requestToPermissions(FolderActivity.this,permissions,MY_MULTIPLE_PERMISSION);
       }
    }

    private void getAllFolder(Activity activity)
    {
        listOfAllFolder = new ArrayList<>();

        Uri uri;
        Cursor cursor;
        int column_index_bucket_name,column_index_bucket_id,column_index_data;

        String absolutePathOfImage;
        String bucketName;
        String bucketId;
        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.Images.Media.BUCKET_DISPLAY_NAME,MediaStore.Images.Media.BUCKET_ID,MediaStore.Images.Thumbnails.DATA};

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, MediaStore.Images.ImageColumns.DATE_ADDED + " DESC");

        column_index_bucket_id = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID);
        column_index_bucket_name = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA);

        while (cursor.moveToNext())
        {
            absolutePathOfImage = cursor.getString(column_index_data);
            bucketName = cursor.getString(column_index_bucket_name);
            bucketId = cursor.getString(column_index_bucket_id);

            Folder folder = new Folder(bucketId,bucketName,absolutePathOfImage,false);

            listOfAllFolder.add(folder);

        }

        HashSet<String> setbucketname = new HashSet<String>();

        for( Folder f : listOfAllFolder )
        {
            if( setbucketname.add( f.getBucketImagesName() ))
            {
                folderList.add( f );
            }
        }
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
            Toast.makeText(this, "Permissions Denied", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    @Override
    public void cbClick(int position,String BucketId,Boolean isSelect,Boolean click)
    {
        /**/
        if(click == false)
        {
            Intent intentGallery = new Intent(this,GalleryActivity.class);
            intentGallery.putExtra("BucketId",BucketId);
            startActivity(intentGallery);
            finish();
        }
        else
        {
            folderList.get(position).isSelected = isSelect;
            this.position=position;
        }
    }
}
