package com.example.android.popularmovies;

/**
 * Created by ninhp on 05-Jul-17.
 */

public class FavoriteItem {
    String movieId;
    String movieTitle;
    String moviePosterPath;

    public FavoriteItem(String movieId,
                        String movieTitle,
                        String moviePosterPath) {
        this.movieId = movieId;
        this.movieTitle = movieTitle;
        this.moviePosterPath = moviePosterPath;
    }

    public String getMovieId() {
        return movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getMoviePosterPath() {
        return moviePosterPath;
    }
}
