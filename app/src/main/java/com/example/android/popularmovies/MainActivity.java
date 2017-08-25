package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    GridView simpleList;
    ArrayList<Item> movieList = new ArrayList<>();
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String criteria = prefs.getString("sortby", "popularity.desc");
        updateMovie(criteria);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);//Menu Resource, Menu
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_setting:
                // Display the fragment as the main content.
                Intent i = new Intent(this, PrefsActivity.class);
                startActivity(i);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                updateMovie(prefs.getString("sortby", "popularity.desc"));
                return true;
            case R.id.favorite_movie:
                Intent intent = new Intent(this, FavoriteActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateMovie(String sortBy) {
        if (Utility.isOnline(getApplicationContext())) {
            FetchMovieTask fetchMovie = new FetchMovieTask();
            fetchMovie.execute(sortBy);
            simpleList = (GridView) findViewById(R.id.simpleGridView);
            myAdapter = new MyAdapter(this, R.layout.grid_view_items, movieList);
            simpleList.setAdapter(myAdapter);
            simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);
                    intent.putExtra("movieName", myAdapter.getItem(position).getMovieName());
                    intent.putExtra("movieUrl", myAdapter.getItem(position).getMovieUrl());
                    intent.putExtra("movieOverview", myAdapter.getItem(position).getMovieOverview());
                    intent.putExtra("movieRating", myAdapter.getItem(position).getMovieRating());
                    intent.putExtra("movieDate", myAdapter.getItem(position).getMovieDate());
                    intent.putExtra("movieId", myAdapter.getItem(position).getMovieId());
                    startActivity(intent);
                }
            });
        } else {
            Context context = getApplicationContext();
            CharSequence text = "No Internet Connection";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<Item>> {
        private ArrayList<Item> getMovieDataFromJson(String movieJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String MOVIE_RESULT = "results";
            ArrayList<Item> resultList = new ArrayList<>();

            JSONObject resultJson = new JSONObject(movieJsonStr);
            JSONArray resultJsonArray = resultJson.getJSONArray(MOVIE_RESULT);


            for (int i = 0; i < resultJsonArray.length(); i++) {
                JSONObject movieInJsonArray = resultJsonArray.getJSONObject(i);
                String releaseDate = movieInJsonArray.getString("release_date");
                if (releaseDate.length() > 3) {
                    releaseDate = movieInJsonArray.getString("release_date").substring(0, 4);
                }
                resultList.add(new Item(movieInJsonArray.getString("poster_path"),
                        movieInJsonArray.getString("original_title"),
                        movieInJsonArray.getString("overview"),
                        movieInJsonArray.getString("vote_average"),
                        releaseDate,
                        movieInJsonArray.getString("id")));


            }
            return resultList;

        }

        @Override
        protected ArrayList<Item> doInBackground(String... params) {
            String apiKey = "f68bffc6174ff40ddc6db10bddc5d216";
            String sortBy = params[0];
            String language = "en-US";
            String inclAdult = "false";

            String movieJsonStr = NetworkUtils.getDataFromInternet(apiKey, sortBy, language, inclAdult);

            try {
                return getMovieDataFromJson(movieJsonStr);
            } catch (JSONException e) {
                Log.e("Error", e.getMessage(), e);
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(ArrayList<Item> resultList) {
            if (resultList != null) {
                myAdapter.clear();
                for (Item temp : resultList) {
                    myAdapter.add(temp);
                }
                // New data is back from the server.  Hooray!
            }
        }
    }
}