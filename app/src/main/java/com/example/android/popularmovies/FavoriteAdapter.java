package com.example.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.data.MovieContract;
import com.squareup.picasso.Picasso;

/**
 * Created by ninhp on 05-Jul-17.
 */

public class FavoriteAdapter extends CursorAdapter {
    public FavoriteAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    /*This is ported from FetchWeatherTask --- but now we go straight from the cursor to the
    string.
 */
    private FavoriteItem convertCursorRowToUXFormat(Cursor cursor) {
        // get row indices for our cursor
        int idx_movie_id = cursor.getColumnIndex(MovieContract.FavoriteMovie.COLUMN_MOVIE_ID);
        int idx_movie_title = cursor.getColumnIndex(MovieContract.FavoriteMovie.COLUMN_MOVIE_TITLE);
        int idx_poster_path = cursor.getColumnIndex(MovieContract.FavoriteMovie.COLUMN_MOVIE_POSTER_PATH);

        FavoriteItem favoriteItem = new FavoriteItem(cursor.getString(idx_movie_id),
                cursor.getString(idx_movie_title),
                cursor.getString(idx_poster_path));


        return favoriteItem;
    }

    /*
        Remember that these views are reused as needed.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.favorite_item, parent, false);

        return view;
    }

    /*
        This is where we fill-in the views with the contents of the cursor.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // our view is pretty simple here --- just a text view
        // we'll keep the UI functional with a simple (and slow!) binding.

        TextView movieTitle = (TextView) view.findViewById(R.id.favorite_movie_title);
        ImageView moviePoster = (ImageView) view.findViewById(R.id.favorite_image_view);
        movieTitle.setText(convertCursorRowToUXFormat(cursor).getMovieTitle());
        Picasso.with(context).load("http://image.tmdb.org/t/p/w185/" + convertCursorRowToUXFormat(cursor).getMoviePosterPath()).into(moviePoster);
    }
}
