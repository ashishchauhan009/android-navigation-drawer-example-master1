package net.simplifiedcoding.navigationdrawerexample.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vibes on 6/3/17.
 */

public class VisionModel implements Serializable {

    private String status;
    private String tag;
    private ArrayList<Vision> data;
    private ArrayList<Mis> mis;

    public ArrayList<Mis> getMis() {
        return mis;
    }

    public void setMis(ArrayList<Mis> mis) {
        this.mis = mis;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public ArrayList<Vision> getData() {
        return data;
    }

    public void setData(ArrayList<Vision> data) {
        this.data = data;
    }

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "VisionModel{" +
                "status='" + status + '\'' +
                ", tag='" + tag + '\'' +
                ", data=" + data +
                ", mis=" + mis +
                '}';
    }
}
