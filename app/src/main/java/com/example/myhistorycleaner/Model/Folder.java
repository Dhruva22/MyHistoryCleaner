package com.example.myhistorycleaner.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mishtiii on 18-01-2017.
 */

public class Folder implements Parcelable
{
    public String bucketImagesId,bucketImagesName,absolutePathOfImage;
    public Boolean isSelected;

    public String getBucketImagesId() {
        return bucketImagesId;
    }

    public void setBucketImagesId(String bucketImagesId) {
        this.bucketImagesId = bucketImagesId;
    }

    public String getBucketImagesName() {
        return bucketImagesName;
    }

    public void setBucketImagesName(String bucketImagesName) {
        this.bucketImagesName = bucketImagesName;
    }

    public String getAbsolutePathOfImage() {
        return absolutePathOfImage;
    }

    public void setAbsolutePathOfImage(String absolutePathOfImage) {
        this.absolutePathOfImage = absolutePathOfImage;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public Folder(String bucketImagesId, String bucketImagesName, String absolutePathOfImage, Boolean isSelected) {

        this.bucketImagesId = bucketImagesId;
        this.bucketImagesName = bucketImagesName;
        this.absolutePathOfImage = absolutePathOfImage;
        this.isSelected = isSelected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bucketImagesId);
        dest.writeString(this.bucketImagesName);
        dest.writeString(this.absolutePathOfImage);
        dest.writeValue(this.isSelected);
    }

    protected Folder(Parcel in) {
        this.bucketImagesId = in.readString();
        this.bucketImagesName = in.readString();
        this.absolutePathOfImage = in.readString();
        this.isSelected = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Parcelable.Creator<Folder> CREATOR = new Parcelable.Creator<Folder>() {
        @Override
        public Folder createFromParcel(Parcel source) {
            return new Folder(source);
        }

        @Override
        public Folder[] newArray(int size) {
            return new Folder[size];
        }
    };
}
