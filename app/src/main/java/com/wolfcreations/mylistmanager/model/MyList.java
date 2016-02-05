package com.wolfcreations.mylistmanager.model;

/**
 * Created by Gebruiker on 31/10/2015.
 */
public class MyList {
    private int mId;
    private String mName;
    private int mPriority;
    private String mCategory;

    public String getCategory() {
        return mCategory;
    }
    public void setCategory(String category) {
        mCategory = category;
    }

    public MyList(int id, String name, int priority) {
        mId = id;
        mName = name;
        mPriority = priority;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getPriority() {
        return mPriority;
    }

    public void setPriority(int priority) {
        mPriority = priority;
    }

    @Override
    public String toString() {
        return mName;
    }
}
