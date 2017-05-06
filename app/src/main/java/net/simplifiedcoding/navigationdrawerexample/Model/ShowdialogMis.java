package net.simplifiedcoding.navigationdrawerexample.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ShowdialogMis implements Serializable {

    @SerializedName("tag")
    @Expose
    private String tag;
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("data")
    @Expose
    private ArrayList<DataMis> data;


    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "ShowdialogMis{" +
                "status='" + status + '\'' +
                '}';
    }

    public ArrayList<DataMis> getData() {
        return data;
    }

    public void setData(ArrayList<DataMis> data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
