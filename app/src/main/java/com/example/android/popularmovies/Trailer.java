package com.example.android.popularmovies;

/**
 * Created by ninhp on 03-Jul-17.
 */

public class Trailer {
    String movieId, trailerName, key;

    public Trailer(String movieId, String trailerName, String key) {
        this.movieId = movieId;
        this.trailerName = trailerName;
        this.key = key;
    }

    public String getMovieId() {
        return this.movieId;
    }

    public String getTrailerName() {
        return this.trailerName;
    }

    public String getKey() {
        return this.key;
    }
}
