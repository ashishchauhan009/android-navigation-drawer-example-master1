package net.simplifiedcoding.navigationdrawerexample.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by vibes on 3/4/17.
 */

public class Contribution implements Serializable {

    private String status;
    private String count;
    private String tag;
    private ArrayList<State> state;
    private ArrayList<Contribute> contribute;
    private ArrayList<Contribute> recentcontribute;
    private ArrayList<Contribute> Bar_Chart;
    private Piechart Pie_Chart;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<State> getState() {
        return state;
    }

    public void setState(ArrayList<State> state) {
        this.state = state;
    }

    public ArrayList<Contribute> getContribute() {
        return contribute;
    }

    public void setContribute(ArrayList<Contribute> contribute) {
        this.contribute = contribute;
    }

    public ArrayList<Contribute> getRecentcontribute() {
        return recentcontribute;
    }

    public void setRecentcontribute(ArrayList<Contribute> recentcontribute) {
        this.recentcontribute = recentcontribute;
    }

    public Piechart getPie_Chart() {
        return Pie_Chart;
    }

    public void setPie_Chart(Piechart pie_Chart) {
        Pie_Chart = pie_Chart;
    }

    public ArrayList<Contribute> getBar_Chart() {
        return Bar_Chart;
    }

    public void setBar_Chart(ArrayList<Contribute> bar_Chart) {
        Bar_Chart = bar_Chart;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
