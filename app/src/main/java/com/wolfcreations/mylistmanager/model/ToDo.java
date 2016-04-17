package com.wolfcreations.mylistmanager.model;

import java.util.Date;

/**
 * Created by Gebruiker on 10/01/2016.
 */
public class ToDo extends MyListItem {
    private boolean mDone ;
    private Date mDueDate;

    public boolean isDone() {
        return mDone;
    }

    public void setDone(boolean isdone) {
        mDone = isdone;
    }


    public Date getDueDate() {
        return mDueDate;
    }

    public void setDueDate(Date dueDate) {
        mDueDate = dueDate;
    }


    public ToDo(MyList alist,  Integer id, String name, String comment) {
        super(alist, id, name,comment);
    }
}
