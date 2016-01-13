package com.wolfcreations.mylistmanager;

/**
 * Created by Gebruiker on 31/10/2015.
 */
public class ListItem {
    private int mId;
    private String mName;
    private String mComment;
    private int mRating;
    private int mPriority;
    private String mUrl;
    private String mPicture;

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

    public String getComment() {
        return mComment;
    }

    public void setComment(String comment) {
        mComment = comment;
    }

    public int getRating() {
        return mRating;
    }

    public void setRating(int rating) {
        mRating = rating;
    }

    public int getpriotity() {
        return mPriority;
    }

    public void setpriotity(int mpriotity) {
        this.mPriority = mpriotity;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getPicture() {
        return mPicture;
    }

    public void setPicture(String picture) {
        mPicture = picture;
    }
}
