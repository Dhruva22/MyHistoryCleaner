package com.example.myhistorycleaner.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mishtiii on 18-01-2017.
 */

public class ImagesVideo implements Parcelable {
    public String imageId,imagePath,bucketImagesId,bucketImagesName;
    public Boolean isSelected;

    public ImagesVideo(String imageId, String imagePath, String bucketImagesId, String bucketImagesName, Boolean isSelected) {
        this.imageId = imageId;
        this.imagePath = imagePath;
        this.bucketImagesId = bucketImagesId;
        this.bucketImagesName = bucketImagesName;
        this.isSelected = isSelected;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

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

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageId);
        dest.writeString(this.imagePath);
        dest.writeString(this.bucketImagesId);
        dest.writeString(this.bucketImagesName);
        dest.writeValue(this.isSelected);
    }

    protected ImagesVideo(Parcel in) {
        this.imageId = in.readString();
        this.imagePath = in.readString();
        this.bucketImagesId = in.readString();
        this.bucketImagesName = in.readString();
        this.isSelected = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Parcelable.Creator<ImagesVideo> CREATOR = new Parcelable.Creator<ImagesVideo>() {
        @Override
        public ImagesVideo createFromParcel(Parcel source) {
            return new ImagesVideo(source);
        }

        @Override
        public ImagesVideo[] newArray(int size) {
            return new ImagesVideo[size];
        }
    };
}
