package net.simplifiedcoding.navigationdrawerexample.Model;

/**
 * Created by Vibes37 on 18/3/2017.
 */


        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

        import java.util.List;

public class PendingTaskDetail {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("tag")
    @Expose
    private String tag;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("response")
    @Expose
    private List<Response> response = null;

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

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public List<Response> getResponse() {
        return response;
    }

    public void setResponse(List<Response> response) {
        this.response = response;
    }


    public class Datum {

        @SerializedName("task_id")
        @Expose
        private String taskId;
        @SerializedName("task_status")
        @Expose
        private String taskStatus;

        @SerializedName("task_subject")
        @Expose
        private String taskSubject;
        @SerializedName("task_description")
        @Expose
        private String taskDescription;
        @SerializedName("created_on")
        @Expose
        private String createdOn;
        @SerializedName("created_by")
        @Expose
        private String createdBy;
        @SerializedName("task_completion_date")
        @Expose
        private String taskCompletionDate;
        @SerializedName("task_completed_on")
        @Expose
        private String task_completed_on;
        @SerializedName("doc")
        @Expose
        private String doc;


        public String getTask_completed_on() {
            return task_completed_on;
        }

        public void setTask_completed_on(String task_completed_on) {
            this.task_completed_on = task_completed_on;
        }



        public String getDoc() {
            return doc;
        }

        public void setDoc(String doc) {
            this.doc = doc;
        }


        public String getTaskCompletionDate() {
            return taskCompletionDate;
        }

        public void setTaskCompletionDate(String taskCompletionDate) {
            this.taskCompletionDate = taskCompletionDate;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public String getTaskSubject() {
            return taskSubject;
        }

        public void setTaskSubject(String taskSubject) {
            this.taskSubject = taskSubject;
        }

        public String getTaskDescription() {
            return taskDescription;
        }

        public void setTaskDescription(String taskDescription) {
            this.taskDescription = taskDescription;
        }

        public String getCreatedOn() {
            return createdOn;
        }

        public void setCreatedOn(String createdOn) {
            this.createdOn = createdOn;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }
        public String getTaskStatus() {
            return taskStatus;
        }

        public void setTaskStatus(String taskStatus) {
            this.taskStatus = taskStatus;
        }

    }


    public class Response {

        @SerializedName("remark")
        @Expose
        private String remark;
        @SerializedName("created_by")
        @Expose
        private String createdBy;
        @SerializedName("updated_on")
        @Expose
        private String updatedOn;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("doc")
        @Expose
        private String doc;
        @SerializedName("previous_remark")
        @Expose
        private String previousRemark;
        @SerializedName("previous_attach")
        @Expose

        private String previousAttach;
        @SerializedName("previous_remark_added")
        @Expose
        private String previousRemarkAdded;

        public String getPreviousRemark() {
            return previousRemark;
        }

        public void setPreviousRemark(String previousRemark) {
            this.previousRemark = previousRemark;
        }

        public String getPreviousRemarkAdded() {
            return previousRemarkAdded;
        }

        public void setPreviousRemarkAdded(String previousRemarkAdded) {
            this.previousRemarkAdded = previousRemarkAdded;
        }

        public String getPreviousAttach() {
            return previousAttach;
        }

        public void setPreviousAttach(String previousAttach) {
            this.previousAttach = previousAttach;
        }


        public String getDoc() {
            return doc;
        }

        public void setDoc(String doc) {
            this.doc = doc;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getUpdatedOn() {
            return updatedOn;
        }

        public void setUpdatedOn(String updatedOn) {
            this.updatedOn = updatedOn;
        }

    }

    @Override
    public String toString() {
        return "PendingTaskDetail{" +
                "status='" + status + '\'' +
                ", tag='" + tag + '\'' +
                ", data=" + data +
                ", response=" + response +
                '}';
    }
}