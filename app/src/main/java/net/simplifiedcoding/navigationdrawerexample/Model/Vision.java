package net.simplifiedcoding.navigationdrawerexample.Model;

import java.io.Serializable;

/**
 * Created by vibes on 6/3/17.
 */

public class Vision implements Serializable {

    String nid;
    String body;
    String title;
    String field_url;
    String contact;

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getField_url() {
        return field_url;
    }

    public void setField_url(String field_url) {
        this.field_url = field_url;
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

    public String getNid() {

        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    @Override
    public String toString() {
        return "Vision{" +
                "nid='" + nid + '\'' +
                ", body='" + body + '\'' +
                ", title='" + title + '\'' +
                ", field_url='" + field_url + '\'' +
                ", contact='" + contact + '\'' +
                '}';
    }
}
