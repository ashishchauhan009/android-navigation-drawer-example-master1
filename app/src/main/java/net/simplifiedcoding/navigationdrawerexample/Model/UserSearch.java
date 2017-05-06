package net.simplifiedcoding.navigationdrawerexample.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by vibes on 16/3/17.
 */

public class UserSearch implements Serializable {
   private String status;
   private String tag;
   private ArrayList<Search> data;

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

    public ArrayList<Search> getData() {
        return data;
    }

    public void setData(ArrayList<Search> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "UserSearch{" +
                "status='" + status + '\'' +
                ", tag='" + tag + '\'' +
                ", data=" + data +
                '}';
    }
}
