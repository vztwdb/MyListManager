package com.wolfcreations.mylistmanager;

/**
 * Created by Gebruiker on 31/10/2015.
 */
public class Lijst {
    private int mId;
    private String mNaam;
    private int mBelangrijk;

    public Lijst(int id, String naam, int belangrijk) {
        mId = id;
        mNaam = naam;
        mBelangrijk= belangrijk;
    }

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

    public int getBelangrijk() {
        return mBelangrijk;
    }

    public void setBelangrijk(int belangrijk) {
        mBelangrijk = belangrijk;
    }
}
