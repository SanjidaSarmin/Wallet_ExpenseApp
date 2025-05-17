package com.example.wallet.Model;

public class Task {
    private int id;
    private String taskText;
    private boolean isDone;

    public Task(int id, String taskText, boolean isDone) {
        this.id = id;
        this.taskText = taskText;
        this.isDone = isDone;
    }

    public Task(String taskText) {
        this.taskText = taskText;
        this.isDone = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTaskText() {
        return taskText;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setTaskText(String taskText) {
        this.taskText = taskText;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
