package net.simplifiedcoding.navigationdrawerexample.Model;

import java.io.Serializable;

/**
 * Created by vibes on 7/3/17.
 */

public class CampaignCommon implements Serializable {

    private String image;
    private String nid;
    private String title;
    private String field_url;
    private String body;
    private String created;


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "CampaignCommon{" +
                "image='" + image + '\'' +
                ", nid='" + nid + '\'' +
                ", title='" + title + '\'' +
                ", field_url='" + field_url + '\'' +
                ", body='" + body + '\'' +
                ", created='" + created + '\'' +
                '}';
    }
}
