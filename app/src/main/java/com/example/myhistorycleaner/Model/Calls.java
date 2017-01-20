package com.example.myhistorycleaner.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mishtiii on 13-01-2017.
 */

public class Calls implements Parcelable {
    public String Id,img,Name,Number,Type,Date,Duration;
    public boolean isSelected;

    public Calls()
    {
    }

    public Calls(String id, String name, String number, String type, String date, String duration, boolean isSelected) {
        Id = id;
        Name = name;
        Number = number;
        Type = type;
        Date = date;
        Duration = duration;
        this.isSelected = isSelected;
    }

    public Calls(String id, String img, String name, String number, String type, String date, String duration)
    {
        Id = id;
        this.img = img;
        Name = name;
        Number = number;
        Type = type;
        Date = date;
        Duration = duration;
    }

    public Calls(String id, String img, String name, String number, String type, String date, String duration, boolean isSelected)
    {
        Id = id;
        this.img = img;
        Name = name;
        Number = number;
        Type = type;
        Date = date;
        Duration = duration;
        this.isSelected = isSelected;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Id);
        dest.writeString(this.img);
        dest.writeString(this.Name);
        dest.writeString(this.Number);
        dest.writeString(this.Type);
        dest.writeString(this.Date);
        dest.writeString(this.Duration);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    protected Calls(Parcel in) {
        this.Id = in.readString();
        this.img = in.readString();
        this.Name = in.readString();
        this.Number = in.readString();
        this.Type = in.readString();
        this.Date = in.readString();
        this.Duration = in.readString();
        this.isSelected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Calls> CREATOR = new Parcelable.Creator<Calls>() {
        @Override
        public Calls createFromParcel(Parcel source) {
            return new Calls(source);
        }

        @Override
        public Calls[] newArray(int size) {
            return new Calls[size];
        }
    };
}
