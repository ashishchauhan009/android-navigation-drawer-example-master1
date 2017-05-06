package net.simplifiedcoding.navigationdrawerexample.Model;

import java.io.Serializable;

/**
 * Created by vibes on 12/3/17.
 */

public class LoginModel implements Serializable {

    private String tag;
    private String status;
    private String id;
    private Login data;


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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Login getData() {
        return data;
    }

    public void setData(Login data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "LoginModel{" +
                "tag='" + tag + '\'' +
                ", status='" + status + '\'' +
                ", id='" + id + '\'' +
                ", data=" + data +
                '}';
    }
}
