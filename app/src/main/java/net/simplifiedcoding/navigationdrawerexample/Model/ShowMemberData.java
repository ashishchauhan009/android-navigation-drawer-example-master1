package net.simplifiedcoding.navigationdrawerexample.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Vibes37 on 19/3/2017.
 */
public class ShowMemberData {

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

        @SerializedName(value = "Member", alternate = {"CITOffice"})
        @Expose
        private String member;

        public String getMember() {
            return member;
        }

        public void setMember(String member) {
            this.member = member;
        }

    }


}