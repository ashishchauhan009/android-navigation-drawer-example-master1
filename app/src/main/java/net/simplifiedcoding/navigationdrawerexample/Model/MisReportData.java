package net.simplifiedcoding.navigationdrawerexample.Model;

/**
 * Created by Vibes37 on 20/3/2017.
 */


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MisReportData implements Parcelable{

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("tag")
    @Expose
    private String tag;
    @SerializedName("date")
    @Expose
    private String date;


    @SerializedName("data")
    @Expose
    private List<Datum> data = null;


    @SerializedName("clickable")
    @Expose
    private String clickable;

    protected MisReportData(Parcel in) {
        status = in.readString();
        tag = in.readString();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static final Creator<MisReportData> CREATOR = new Creator<MisReportData>() {
        @Override
        public MisReportData createFromParcel(Parcel in) {
            return new MisReportData(in);
        }

        @Override
        public MisReportData[] newArray(int size) {
            return new MisReportData[size];
        }
    };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(status);
        parcel.writeString(tag);
    }


    public String getClickable() {
        return clickable;
    }

    public void setClickable(String clickable) {
        this.clickable = clickable;
    }
}
