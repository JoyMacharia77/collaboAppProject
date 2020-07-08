package com.joymacharia.collaboapp;

public class Task {

    private final int taskImage;
    private String taskTitle;
    private String taskDeadline;


    /*Create a constructor for the recipe data model
     * Pass the parameters recipeImage, recipeTitle and recipeDescription*/

    Task(int taskImage, String taskTitle, String taskDeadline)// String recipeIngredients)
    {
        this.taskImage = taskImage;
        this.taskTitle = taskTitle;
        this.taskDeadline = taskDeadline;

    }
    /* Create the getters and return the specific object*/

    public int getTaskImage(){ return  taskImage;}
    public String getTaskTitle()
    {
        return taskTitle;
    }
    public String getTaskDeadline()
    {
        return taskDeadline;
    }

}
