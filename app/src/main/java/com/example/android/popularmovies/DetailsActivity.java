package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {
    GridView trailerListView;
    ArrayList<Trailer> trailerName = new ArrayList<>();
    String movieId, movieName, movieUrl;
    TrailerAdapter trailerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        movieName = intent.getStringExtra("movieName");
        movieUrl = intent.getStringExtra("movieUrl");
        String movieOverview = intent.getStringExtra("movieOverview");
        String movieRating = intent.getStringExtra("movieRating");
        String movieDate = intent.getStringExtra("movieDate");
        movieId = intent.getStringExtra("movieId");
        updateTrailer(movieId);

        TextView movieNameTextView = (TextView) findViewById(R.id.movie_name);
        ImageView imageView = (ImageView) findViewById(R.id.movie_poster);
        TextView movieDateTextView = (TextView) findViewById(R.id.movie_year);
        TextView movieDurationTextView = (TextView) findViewById(R.id.movie_year);
        TextView movieRatingTextView = (TextView) findViewById(R.id.movie_rating);
        TextView movieOverviewTextView = (TextView) findViewById(R.id.movie_synopsis);


        movieNameTextView.setText(movieName);
        Picasso.with(getApplicationContext()).load("http://image.tmdb.org/t/p/w185/" + movieUrl).into(imageView);
        movieDateTextView.setText(movieDate);
        movieRatingTextView.setText(movieRating + "/10");
        movieOverviewTextView.setText(movieOverview);
//        trailerListView.setAdapter(new TrailerAdapter(this, trailer_list));
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
                return true;
            case R.id.favorite_movie:
                Intent fav = new Intent(this, FavoriteActivity.class);
                startActivity(fav);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onReviewClick(View view) {
        Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
        startActivity(intent);
    }

    public void onFavoriteClick(View view) {
        CharSequence text;
        Context context = getApplicationContext();
        if (Utility.checkFavorite(context, movieId)) {
            text = "Favorite is already added";
        } else {
            Utility.addFavorite(getApplicationContext(), movieId, movieName, movieUrl);
            text = "Favorite Added";
        }
        toastNoti(context, text);


    }

    public void toastNoti(Context context, CharSequence text) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void updateTrailer(String movieId) {
        if (Utility.isOnline(getApplicationContext())) {
            GetTrailerTask getTrailer = new GetTrailerTask();
            getTrailer.execute(movieId);
            trailerListView = (GridView) findViewById(R.id.trailerGridView);
            trailerAdapter = new TrailerAdapter(getApplicationContext(), R.layout.trailer_list_view, trailerName);
            trailerListView.setAdapter(trailerAdapter);
            trailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String key = trailerAdapter.getItem(position).getKey();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key));
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

    public class GetTrailerTask extends AsyncTask<String, Void, ArrayList<Trailer>> {
        private ArrayList<Trailer> getTrailerDataFromJson(String trailerJsonStr)
                throws JSONException {

            // These are the names of the JSON objects that need to be extracted.
            final String TRAILER_RESULT = "results";
            ArrayList<Trailer> resultList = new ArrayList<>();

            JSONObject resultJson = new JSONObject(trailerJsonStr);
            JSONArray resultJsonArray = resultJson.getJSONArray(TRAILER_RESULT);
            if (resultJsonArray == null) {
                Log.v("resultJsonArray", resultJsonArray + "");
            }


            for (int i = 0; i < resultJsonArray.length(); i++) {
                JSONObject trailerInJsonArray = resultJsonArray.getJSONObject(i);
                resultList.add(new Trailer(trailerInJsonArray.getString("id"),
                        trailerInJsonArray.getString("name"),
                        trailerInJsonArray.getString("key")));

            }
            return resultList;

        }

        @Override
        protected ArrayList<Trailer> doInBackground(String... movieId) {
            String apiKey = "f68bffc6174ff40ddc6db10bddc5d216";
            String language = "en-US";

            String trailerJsonStr = NetworkUtils.getDetailsData(apiKey, language, movieId[0], "videos");

            if (trailerJsonStr == null) {
                return null;
            }

            try {
                return getTrailerDataFromJson(trailerJsonStr);
            } catch (Throwable e) {
                Log.e("Error", e.getMessage(), e);
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(ArrayList<Trailer> resultList) {
            if (resultList != null) {
                trailerAdapter.clear();
                for (Trailer temp : resultList) {
                    trailerAdapter.add(temp);
                }
            }
        }
    }
}
