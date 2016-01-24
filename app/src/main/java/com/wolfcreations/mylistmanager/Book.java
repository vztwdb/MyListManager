package com.wolfcreations.mylistmanager;

/**
 * Created by Gebruiker on 10/01/2016.
 */
public class Book extends MyListItem {
    private String Title;
    private String Autor;
    private int Year;
    private String Review;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getAutor() {
        return Autor;
    }

    public void setAutor(String autor) {
        Autor = autor;
    }

    public int getYear() {
        return Year;
    }

    public void setYear(int year) {
        Year = year;
    }

    public String getReview() {
        return Review;
    }

    public void setReview(String review) {
        Review = review;
    }

    public Book(Integer id, String name, String comment) {
        super(id, name,comment);
    }


}
