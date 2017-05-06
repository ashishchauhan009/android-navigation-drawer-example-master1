package net.simplifiedcoding.navigationdrawerexample.Model;

import java.io.Serializable;

/**
 * Created by vibes on 4/3/17.
 */

public class CommonHomeModel implements Serializable {


    private String nid;
    private String body;
    private String title;
    private String field_url;
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getField_url() {
        return field_url;
    }

    public void setField_url(String field_url) {
        this.field_url = field_url;
    }



    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    public String toString() {
        return "CommonHomeModel{" +
                "nid='" + nid + '\'' +
                ", body='" + body + '\'' +
                ", title='" + title + '\'' +
                ", field_url='" + field_url + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
