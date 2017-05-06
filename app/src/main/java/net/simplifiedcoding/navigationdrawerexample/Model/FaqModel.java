package net.simplifiedcoding.navigationdrawerexample.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vibes on 6/3/17.
 */

public class FaqModel implements Serializable {
    private String status;
    private ArrayList<FaqCommon> faq;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<FaqCommon> getFaq() {
        return faq;
    }

    public void setFaq(ArrayList<FaqCommon> faq) {
        this.faq = faq;
    }

    @Override
    public String toString() {
        return "FaqModel{" +
                "status='" + status + '\'' +
                ", faq=" + faq +
                '}';
    }
}
