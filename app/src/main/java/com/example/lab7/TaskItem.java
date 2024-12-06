package com.example.lab7;

public class TaskItem {
    private String text;
    private boolean checked;

    public TaskItem(String text, boolean checked) {
        this.text = text;
        this.checked = checked;
    }

    public String getText() { return text; }
    public boolean isChecked() { return checked; }
    public void setChecked(boolean checked) { this.checked = checked; }
}