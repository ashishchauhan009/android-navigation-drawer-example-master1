package net.simplifiedcoding.navigationdrawerexample.Model;

/**
 * Created by Vibes37 on 19/3/2017.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShowPccitData {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("tag")
    @Expose
    private String tag;
    @SerializedName("data")
    @Expose
    private List<String> data = null;

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

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

}

