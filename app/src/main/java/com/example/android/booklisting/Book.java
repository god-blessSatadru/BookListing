package com.example.android.booklisting;

/**
 * Created by baba on 9/8/2016.
 */
public class Book {
    private String mAuthor;
    private String mTitle;

    public Book(String mAuthor, String mTitle) {
        this.mAuthor = mAuthor;
        this.mTitle = mTitle;
    }


    public String getmAuthor() {
        return mAuthor;
    }


    public String getmTitle() {
        return mTitle;
    }
}
