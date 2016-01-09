package com.wolfcreations.mylistmanager;

/**
 * Created by Gebruiker on 31/10/2015.
 */
public class ListItem {
    private int mId;
    private String mNaam;
    private String mBeschrijving;
    private int mRating;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getNaam() {
        return mNaam;
    }

    public void setNaam(String naam) {
        mNaam = naam;
    }

    public String getBeschrijving() {
        return mBeschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        mBeschrijving = beschrijving;
    }

    public int getRating() {
        return mRating;
    }

    public void setRating(int rating) {
        mRating = rating;
    }
}
