package net.simplifiedcoding.navigationdrawerexample.Model;

import java.io.Serializable;



public class State implements Serializable {

    private String state_id;
    private String state_name;

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }
}