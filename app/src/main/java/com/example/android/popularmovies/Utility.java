package com.example.android.popularmovies;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.example.android.popularmovies.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ninhp on 03-Jul-17.
 */

public class Utility {
    public static boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static ArrayList<ReviewItem> getReviewDataFromJson(String trailerJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String TRAILER_RESULT = "results";
        ArrayList<ReviewItem> resultList = new ArrayList<>();

        JSONObject resultJson = new JSONObject(trailerJsonStr);
        JSONArray resultJsonArray = resultJson.getJSONArray(TRAILER_RESULT);


        for (int i = 0; i < resultJsonArray.length(); i++) {
            JSONObject trailerInJsonArray = resultJsonArray.getJSONObject(i);
            resultList.add(new ReviewItem(trailerInJsonArray.getString("author"), trailerInJsonArray.getString("content")));

        }
        return resultList;

    }

    public static boolean checkFavorite(Context mContext, String movieId) {
        // First, check if the location with this city name exists in the db
        Cursor movieCursor = mContext.getContentResolver().query(
                MovieContract.FavoriteMovie.CONTENT_URI,
                new String[]{MovieContract.FavoriteMovie._ID},
                MovieContract.FavoriteMovie.COLUMN_MOVIE_ID + " = ?",
                new String[]{movieId},
                null);
        return movieCursor.moveToFirst();
    }

    public static long addFavorite(Context mContext, String movieId, String movieTitle, String posterPath) {
        long movieDbId;
        Cursor movieCursor = mContext.getContentResolver().query(
                MovieContract.FavoriteMovie.CONTENT_URI,
                new String[]{MovieContract.FavoriteMovie._ID},
                MovieContract.FavoriteMovie.COLUMN_MOVIE_ID + " = ?",
                new String[]{movieId},
                null);

        if (movieCursor.moveToFirst()) {
            int locationIdIndex = movieCursor.getColumnIndex(MovieContract.FavoriteMovie._ID);
            movieDbId = movieCursor.getLong(locationIdIndex);
        } else {
            // Now that the content provider is set up, inserting rows of data is pretty simple.
            // First create a ContentValues object to hold the data you want to insert.
            ContentValues movieValues = new ContentValues();

            // Then add the data, along with the corresponding name of the data type,
            // so the content provider knows what kind of value is being inserted.
            movieValues.put(MovieContract.FavoriteMovie.COLUMN_MOVIE_ID, movieId);
            movieValues.put(MovieContract.FavoriteMovie.COLUMN_MOVIE_TITLE, movieTitle);
            movieValues.put(MovieContract.FavoriteMovie.COLUMN_MOVIE_POSTER_PATH, posterPath);

            // Finally, insert location data into the database.
            Uri insertedUri = mContext.getContentResolver().insert(
                    MovieContract.FavoriteMovie.CONTENT_URI,
                    movieValues
            );

            // The resulting URI contains the ID for the row.  Extract the locationId from the Uri.
            movieDbId = ContentUris.parseId(insertedUri);
        }

        movieCursor.close();
        // Wait, that worked?  Yes!
        return movieDbId;
    }
}
