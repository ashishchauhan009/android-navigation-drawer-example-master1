package net.simplifiedcoding.navigationdrawerexample.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Vibes37 on 17/3/2017.
 */

public class DashboardData {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("tag")
    @Expose
    private String tag;
    @SerializedName("data")
    @Expose
    private Data data;

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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
    public class Data {

        @SerializedName("assignedbyme")
        @Expose
        private Assignedbyme assignedbyme;
        @SerializedName("taskforme")
        @Expose
        private Taskforme taskforme;

        public Assignedbyme getAssignedbyme() {
            return assignedbyme;
        }

        public void setAssignedbyme(Assignedbyme assignedbyme) {
            this.assignedbyme = assignedbyme;
        }

        public Taskforme getTaskforme() {
            return taskforme;
        }

        public void setTaskforme(Taskforme taskforme) {
            this.taskforme = taskforme;
        }

    }

    public class Assignedbyme {

        @SerializedName("open")
        @Expose
        private String open;
        @SerializedName("closed")
        @Expose
        private String closed;
        @SerializedName("pending")
        @Expose
        private String pending;
        @SerializedName("wip")
        @Expose
        private String wip;

        public String getWip() {
            return wip;
        }

        public void setWip(String wip) {
            this.wip = wip;
        }

        public String getOpen() {
            return open;
        }

        public void setOpen(String open) {
            this.open = open;
        }

        public String getClosed() {
            return closed;
        }

        public void setClosed(String closed) {
            this.closed = closed;
        }

        public String getPending() {
            return pending;
        }

        public void setPending(String pending) {
            this.pending = pending;
        }

    }




    public class Taskforme {

        @SerializedName("open")
        @Expose
        private String open;
        @SerializedName("closed")
        @Expose
        private String closed;
        @SerializedName("pending")
        @Expose
        private String pending;
        @SerializedName("complete")
        @Expose
        private String complete;

        public String getComplete() {
            return complete;
        }

        public void setComplete(String complete) {
            this.complete = complete;
        }

        public String getOpen() {
            return open;
        }

        public void setOpen(String open) {
            this.open = open;
        }

        public String getClosed() {
            return closed;
        }

        public void setClosed(String closed) {
            this.closed = closed;
        }

        public String getPending() {
            return pending;
        }

        public void setPending(String pending) {
            this.pending = pending;
        }

    }

}