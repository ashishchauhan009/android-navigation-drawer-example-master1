package net.simplifiedcoding.navigationdrawerexample.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by vibes on 6/3/17.
 */

public class CampaignModel implements Serializable {
    private String status;
    private ArrayList<CampaignCommon> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<CampaignCommon> getData() {
        return data;
    }

    public void setData(ArrayList<CampaignCommon> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CampaignModel{" +
                "status='" + status + '\'' +
                ", data=" + data +
                '}';
    }
}
