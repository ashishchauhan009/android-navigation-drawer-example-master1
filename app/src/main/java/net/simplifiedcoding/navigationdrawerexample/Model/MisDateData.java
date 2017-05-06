package net.simplifiedcoding.navigationdrawerexample.Model;

/**
 * Created by Vibes37 on 30/3/2017.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MisDateData {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("tag")
    @Expose
    private String tag;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

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

public class Datum {

    @SerializedName("mis_date")
    @Expose
    private String misDate;

    public String getMisDate() {
        return misDate;
    }

    public void setMisDate(String misDate) {
        this.misDate = misDate;
    }

}



}

