package com.wolfcreations.mylistmanager;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Gebruiker on 31/10/2015.
 */
public class MyListItem implements Serializable {
    private Integer mId;
    private String mName;
    private String mComment;
    private Integer mRating;
    private Integer mPriority;
    private String mUrl;
    private String mPicture;
    public TagEnum myTag;
    public Date duedate;

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
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

    public Integer getRating() {
        return mRating;
    }

    public void setRating(Integer rating) {
        mRating = rating;
    }

    public Integer getpriotity() {
        return mPriority;
    }

    public void setpriotity(Integer mpriotity) {
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

    public MyListItem(Integer id, String name, String comment) {
        this.mId = id;
        this.mName = name;
        this.mComment = comment;
    }

    @Override
    public String toString() {
        return mName;
    }
}
