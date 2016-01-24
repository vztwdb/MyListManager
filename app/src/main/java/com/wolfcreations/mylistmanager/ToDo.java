package com.wolfcreations.mylistmanager;

/**
 * Created by Gebruiker on 10/01/2016.
 */
public class ToDo extends MyListItem {
    private String Deadline;

    public String getDeadline() {
        return Deadline;
    }

    public void setDeadline(String deadline) {
        Deadline = deadline;
    }

    public ToDo(Integer id, String name, String comment) {
        super(id, name,comment);
    }
}
