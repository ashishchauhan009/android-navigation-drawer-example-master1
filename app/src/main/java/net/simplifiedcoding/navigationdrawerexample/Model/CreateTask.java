package net.simplifiedcoding.navigationdrawerexample.Model;

import java.io.Serializable;

/**
 * Created by vibes on 17/3/17.
 */

public class CreateTask implements Serializable {

    private String message;
    private String task_subject;
    private String id;
    private String task_for;
    private String task_completion_date;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTask_subject() {
        return task_subject;
    }

    public void setTask_subject(String task_subject) {
        this.task_subject = task_subject;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTask_for() {
        return task_for;
    }

    public void setTask_for(String task_for) {
        this.task_for = task_for;
    }

    public String getTask_completion_date() {
        return task_completion_date;
    }

    public void setTask_completion_date(String task_completion_date) {
        this.task_completion_date = task_completion_date;
    }

    @Override
    public String toString() {
        return "CreateTask{" +
                "message='" + message + '\'' +
                ", task_subject='" + task_subject + '\'' +
                ", id='" + id + '\'' +
                ", task_for='" + task_for + '\'' +
                ", task_completion_date='" + task_completion_date + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
