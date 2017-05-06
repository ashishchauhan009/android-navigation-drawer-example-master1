package net.simplifiedcoding.navigationdrawerexample.Model;

import java.io.Serializable;


public class Piechart implements Serializable {

     private String Spred_Message;
     private String Shared_Training_Material;
     private String Assisted_Somebody;

    public String getSpred_Message() {
        return Spred_Message;
    }

    public void setSpred_Message(String spred_Message) {
        Spred_Message = spred_Message;
    }

    public String getShared_Training_Material() {
        return Shared_Training_Material;
    }

    public void setShared_Training_Material(String shared_Training_Material) {
        Shared_Training_Material = shared_Training_Material;
    }

    public String getAssisted_Somebody() {
        return Assisted_Somebody;
    }

    public void setAssisted_Somebody(String assisted_Somebody) {
        Assisted_Somebody = assisted_Somebody;
    }
}