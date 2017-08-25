package com.example.android.popularmovies;

/**
 * Created by ninhp on 03-Jul-17.
 */

public class ReviewItem {


    String author;
    String content;

    public ReviewItem(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
