package com.wolfcreations.mylistmanager.model;

/**
 * Created by Gebruiker on 10/01/2016.
 */
public class Movie extends MyListItem {
    private String Title;
    private String Producer;
    private int Year;
    private int IMDBRating;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getProducer() {
        return Producer;
    }

    public void setProducer(String producer) {
        Producer = producer;
    }

    public int getYear() {
        return Year;
    }

    public void setYear(int year) {
        Year = year;
    }

    public int getIMDBRating() {
        return IMDBRating;
    }

    public void setIMDBRating(int IMDBRating) {
        this.IMDBRating = IMDBRating;
    }

    public Movie(MyList alist, Integer id, String name, String comment) {
        super(alist, id, name,comment);
    }
}
