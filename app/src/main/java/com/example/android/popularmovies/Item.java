package com.example.android.popularmovies;

/**
 * Created by Gourav on 10-01-2016.
 */
public class Item {

    String movieUrl;
    String movieName;
    String movieOverview;
    String movieRating;
    String movieDate;
    String movieId;

    public Item(String movieUrl, String movieName, String movieOverview, String movieRating, String movieDate, String movieId) {
        this.movieName = movieName;
        this.movieUrl = movieUrl;
        this.movieOverview = movieOverview;
        this.movieRating = movieRating;
        this.movieDate = movieDate;
        this.movieId = movieId;
    }

    public String getMovieUrl() {
        return movieUrl;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getMovieOverview() {
        return movieOverview;
    }

    public String getMovieRating() {
        return movieRating;
    }

    public String getMovieDate() {
        return movieDate;
    }

    public String getMovieId() {
        return movieId;
    }
}