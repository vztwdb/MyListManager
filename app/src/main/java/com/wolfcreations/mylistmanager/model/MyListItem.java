package com.wolfcreations.mylistmanager.model;

import java.io.Serializable;

/**
 * Created by Gebruiker on 31/10/2015.
 */
public class MyListItem implements Serializable {
    private Integer mId;
    private String mName;
    private String mComment;
    private String mDescription;
    private float mRating;
    private Integer mPriority;
    private String mUrl;
    private TagEnum mPicture;
    private String mCategory;
    private MyList mList;

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }

    public MyList getList() { return mList;
    }

    public void setList(MyList list) {  mList = list;
    }
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


    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }


    public float getRating() {
        return mRating;
    }

    public void setRating(float rating) {
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


    public TagEnum getPicture() {
        return mPicture;
    }

    public void setPicture(TagEnum picture) {
        this.mPicture = picture;
    }

    public MyListItem(MyList alist, Integer id, String name, String comment) {
        this.mId = id;
        this.mName = name;
        this.mComment = comment;
        this.mCategory = alist.getCategory();
        mList = alist;
    }

    @Override
    public String toString() {
        return mName;
    }
}
