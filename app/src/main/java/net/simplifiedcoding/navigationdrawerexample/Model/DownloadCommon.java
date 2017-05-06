package net.simplifiedcoding.navigationdrawerexample.Model;

import java.io.Serializable;

/**
 * Created by vibes on 7/3/17.
 */

public class DownloadCommon implements Serializable {

    private String field_url;
    private String title;
    private String body;
    private String nid;


    @Override
    public String toString() {
        return "Download{" +
                "URL='" + field_url + '\'' +
                "nid='" + nid + '\'' +
                "title='" + title + '\'' +
                "bodyText='" + body + '\'' +
                '}';
    }



    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBodyText() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setField_url(String field_url) {
        this.field_url = field_url;
    }

    public String getField_url() {
        return field_url;
    }
}
