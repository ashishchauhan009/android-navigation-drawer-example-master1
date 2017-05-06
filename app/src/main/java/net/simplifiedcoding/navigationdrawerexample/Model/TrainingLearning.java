package net.simplifiedcoding.navigationdrawerexample.Model;

import java.io.Serializable;

/**
 * Created by vibes on 28/2/17.
 */

public class TrainingLearning implements Serializable {

    private String Title;
    private String Link;
    private String pic;

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }
}
