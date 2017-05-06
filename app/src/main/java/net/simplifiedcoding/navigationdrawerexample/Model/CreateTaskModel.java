package net.simplifiedcoding.navigationdrawerexample.Model;

/**
 * Created by vibes on 17/3/17.
 */

public class CreateTaskModel {

    private String status;
    private String tag;
    private CreateTask data;

    public String getStatus() {

        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public CreateTask getData() {
        return data;
    }

    public void setData(CreateTask data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CreateTaskModel{" +
                "status='" + status + '\'' +
                ", tag='" + tag + '\'' +
                ", data=" + data +
                '}';
    }
}
