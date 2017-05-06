package net.simplifiedcoding.navigationdrawerexample.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by vibes on 6/3/17.
 */

public class DownloadModel implements Serializable {
    private String status;
    private ArrayList<DownloadCommon> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<DownloadCommon> getData() {
        return data;
    }

    public void setData(ArrayList<DownloadCommon> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DownloadModel{" +
                "status='" + status + '\'' +
                ", data=" + data +
                '}';
    }
}
