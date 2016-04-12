package com.wolfcreations.mylistmanager.model;

import java.util.Date;

/**
 * Created by Gebruiker on 10/01/2016.
 */
public class ToDo extends MyListItem {
    private Boolean Done;
    private Date mDueDate;

    public Boolean getDone() {
        return Done;
    }

    public void setDone(Boolean done) {
        Done = done;
    }


    public Date isDueDate() {
        return mDueDate;
    }

    public void setDueDate(Date dueDate) {
        mDueDate = dueDate;
    }


    public ToDo(MyList alist,  Integer id, String name, String comment) {
        super(alist, id, name,comment);
    }
}
