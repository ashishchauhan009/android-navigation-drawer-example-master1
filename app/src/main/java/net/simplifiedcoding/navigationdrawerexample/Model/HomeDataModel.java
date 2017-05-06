package net.simplifiedcoding.navigationdrawerexample.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vibes on 4/3/17.
 */

public class HomeDataModel implements Serializable {


    private String status;
    private String tag;
    private List<CommonHomeModel> news;
    private List<CommonHomeModel> dashboard;
    private List<CommonHomeModel> campaign;

    public List<CommonHomeModel> getCampaign() {
        return campaign;
    }

    public void setCampaign(List<CommonHomeModel> campaign) {
        this.campaign = campaign;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public List<CommonHomeModel> getNews() {
        return news;
    }

    public void setNews(List<CommonHomeModel> news) {
        this.news = news;
    }

    public List<CommonHomeModel> getDashboard() {
        return dashboard;
    }

    public void setDashboard(List<CommonHomeModel> dashboard) {
        this.dashboard = dashboard;
    }

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "HomeDataModel{" +
                "status='" + status + '\'' +
                ", tag='" + tag + '\'' +
                ", news=" + news +
                ", dashboard=" + dashboard +
                ", campaign=" + campaign +
                '}';
    }
}
