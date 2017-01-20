package com.example.myhistorycleaner.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mishtiii on 13-01-2017.
 */

public class SmsModel implements Parcelable
{
    public String Id,Name,Msg,Type,Date;
    public boolean isSelected;

    public SmsModel()
    {
    }

    public SmsModel(String id, String name, String msg, String type, String date)
    {
        Id = id;
        Name = name;
        Msg = msg;
        Type = type;
        Date = date;
    }

    public SmsModel(String id, String name, String msg, String type, String date,
                  boolean isSelected) {
        Id = id;
        Name = name;
        Msg = msg;
        Type = type;
        Date = date;
        this.isSelected = isSelected;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String msg) {
        Msg = msg;
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
        dest.writeString(this.Name);
        dest.writeString(this.Msg);
        dest.writeString(this.Type);
        dest.writeString(this.Date);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    protected SmsModel(Parcel in) {
        this.Id = in.readString();
        this.Name = in.readString();
        this.Msg = in.readString();
        this.Type = in.readString();
        this.Date = in.readString();
        this.isSelected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<SmsModel> CREATOR = new Parcelable.Creator<SmsModel>() {
        @Override
        public SmsModel createFromParcel(Parcel source) {
            return new SmsModel(source);
        }

        @Override
        public SmsModel[] newArray(int size) {
            return new SmsModel[size];
        }
    };
}
