package net.simplifiedcoding.navigationdrawerexample.Model;

import java.io.Serializable;

/**
 * Created by vibes on 12/3/17.
 */

public class Mis implements Serializable {

    private String image;
    private String mis_id;
    private String title;
    private String file;
    private String date;
    private String status;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMis_id() {
        return mis_id;
    }

    public void setMis_id(String mis_id) {
        this.mis_id = mis_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Mis{" +
                "image='" + image + '\'' +
                ", mis_id='" + mis_id + '\'' +
                ", title='" + title + '\'' +
                ", file='" + file + '\'' +
                ", date='" + date + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
