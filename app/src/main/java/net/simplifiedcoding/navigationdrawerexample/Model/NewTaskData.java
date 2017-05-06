package net.simplifiedcoding.navigationdrawerexample.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewTaskData {


    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("tag")
    @Expose
    private String tag;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

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


    public class Datum {

        @SerializedName("color")
        @Expose
        private String color;

        @SerializedName("task_id")
        @Expose
        private String taskId;
        @SerializedName("task_subject")
        @Expose
        private String taskSubject;
        @SerializedName("task_description")
        @Expose
        private String taskDescription;
        @SerializedName("created_on")
        @Expose
        private String createdOn;

        @SerializedName("assign_to")
        @Expose
        private String assign_to;
        @SerializedName("created_by")
        @Expose
        private String createdBy;

        @SerializedName("task_status")
        @Expose
        private String taskStatus;

        @SerializedName("task_completed_on")
        @Expose
        private String task_completed_on;

        @SerializedName("task_completion_date")
        @Expose
        private String task_completion_date;

        @SerializedName("assign_to_designation")
        @Expose
        private String assign_to_designation;

        @SerializedName("assign_by_desination")
        @Expose
        private String assign_by_desination;

        public String getAssign_to_designation() {
            return assign_to_designation;
        }

        public void setAssign_to_designation(String assign_to_designation) {
            this.assign_to_designation = assign_to_designation;
        }

        public String getTask_completed_on() {
            return task_completed_on;
        }

        public void setTask_completed_on(String task_completed_on) {
            this.task_completed_on = task_completed_on;
        }

        public String getTaskId() {
            return taskId;
        }

        public String getTask_completion_date() {
            return task_completion_date;
        }

        public void setTask_completion_date(String task_completion_date) {
            this.task_completion_date = task_completion_date;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public String getTaskStatus() {
            return taskStatus;
        }

        public void setTaskStatus(String taskStatus) {
            this.taskStatus = taskStatus;
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

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getAssign_to() {
            return assign_to;
        }

        public void setAssign_to(String assign_to) {
            this.assign_to = assign_to;
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

        public String getAssign_by_desination() {
            return assign_by_desination;
        }

        public void setAssign_by_desination(String assign_by_desination) {
            this.assign_by_desination = assign_by_desination;
        }
    }


}

