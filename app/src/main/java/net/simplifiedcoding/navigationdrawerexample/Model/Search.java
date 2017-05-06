package net.simplifiedcoding.navigationdrawerexample.Model;

import java.io.Serializable;

/**
 * Created by vibes on 16/3/17.
 */

public class Search implements Serializable {

    private String user_name;
    private String user_id;
    private String user_email;
    private String personal_email;
    private String user_phone;
    private String user_designation;
    private String level;
    private String user_level;
    private int showicon;
    private String fav;

    public int getShowicon() {
        return showicon;
    }

    public void setShowicon(int showicon) {
        this.showicon = showicon;
    }

    public String getUser_level() {
        return user_level;
    }

    public void setUser_level(String user_level) {
        this.user_level = user_level;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getFav() {
        return fav;
    }

    public void setFav(String fav) {
        this.fav = fav;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_designation() {
        return user_designation;
    }

    public void setUser_designation(String user_designation) {
        this.user_designation = user_designation;
    }

    @Override
    public String toString() {
        return "Search{" +
                "user_name='" + user_name + '\'' +
                ", user_id='" + user_id + '\'' +
                ", user_email='" + user_email + '\'' +
                ", user_phone='" + user_phone + '\'' +
                ", user_designation='" + user_designation + '\'' +
                ", level='" + level + '\'' +
                ", user_level='" + user_level + '\'' +
                ", showicon='" + showicon + '\'' +
                ", fav='" + fav + '\'' +
                '}';
    }

    public String getPersonal_email() {
        return personal_email;
    }

    public void setPersonal_email(String personal_email) {
        this.personal_email = personal_email;
    }
}
