package com.example.todo_app.Model;
//represent a todolist item ~ an object
public class ToDoModel {
    private int id, status; //task: xong va chua xong
    private String task; //noi dung task

    //getter & setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }
}