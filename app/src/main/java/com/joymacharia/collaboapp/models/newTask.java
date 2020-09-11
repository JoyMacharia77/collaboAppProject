package com.joymacharia.collaboapp.models;

public class newTask {
private int id;
private newUser user;
    private  int taskImage;
    private String taskTitle;
    private String date;
    private String deadline;

    public void setUser(newUser user) {
        this.user = user;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }



    public String getTaskDeadline() {
        return taskDeadline;
    }

    public void setTaskDeadline(String taskDeadline) {
        this.taskDeadline = taskDeadline;
    }

    private String taskDeadline;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTaskImage() {
        return taskImage;
    }

    public void setTaskImage(int taskImage) {
        this.taskImage = taskImage;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public newUser getUser() {
        return user;
    }



}
