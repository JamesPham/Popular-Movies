package com.example.android.popularmovies;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity {
    GridView reviewGridView;
    ReviewAdapter reviewAdapter;
    ArrayList<ReviewItem> reviewList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        updateReview();
    }

    public void updateReview() {
        if (Utility.isOnline(getApplicationContext())) {
            GetReviewTask getReviewTask = new GetReviewTask();
            getReviewTask.execute("283366");
            reviewGridView = (GridView) findViewById(R.id.review_grid_view);
            reviewAdapter = new ReviewAdapter(getApplicationContext(), R.layout.grid_view_review, reviewList);
            reviewGridView.setAdapter(reviewAdapter);
        } else {
            Context context = getApplicationContext();
            CharSequence text = "No Internet Connection";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    public class GetReviewTask extends AsyncTask<String, Void, ArrayList<ReviewItem>> {

        @Override
        protected ArrayList<ReviewItem> doInBackground(String... movieId) {
            String apiKey = "f68bffc6174ff40ddc6db10bddc5d216";
            String language = "en-US";

            String reviewJsonStr = NetworkUtils.getDetailsData(apiKey, language, movieId[0], "reviews");

            try {
                return Utility.getReviewDataFromJson(reviewJsonStr);
            } catch (JSONException e) {
                Log.e("Error", e.getMessage(), e);
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(ArrayList<ReviewItem> resultList) {
            if (resultList != null) {
                reviewAdapter.clear();
                for (ReviewItem temp : resultList) {
                    reviewAdapter.add(temp);
                }
            }
        }
    }
}
