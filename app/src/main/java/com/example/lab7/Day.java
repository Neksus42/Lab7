package com.example.lab7;

import java.util.List;

public class Day {
    private int id;
    private String name;
    private String date;
    private List<TaskItem> tasks;

    public Day(int id, String name, String date, List<TaskItem> tasks) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.tasks = tasks;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDate() { return date; }
    public List<TaskItem> getTasks() { return tasks; }
}