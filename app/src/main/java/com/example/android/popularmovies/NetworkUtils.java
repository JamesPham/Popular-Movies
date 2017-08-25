package com.example.android.popularmovies;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ninhp on 6/17/2017.
 */

public final class NetworkUtils {

    public static String getDataFromInternet(String apiKey, String sortBy, String language, String inclAdult) {
        final String MOVIE_BASE_URL =
                "https://api.themoviedb.org/3/discover/movie?";
        final String APIKEY_PARAM = "api_key";
        final String SORTBY_PARAM = "sort_by";
        final String LANGUAGE_PARAM = "language";
        final String ADULT_PARAM = "include_adult";

        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendQueryParameter(APIKEY_PARAM, apiKey)
                .appendQueryParameter(SORTBY_PARAM, sortBy)
                .appendQueryParameter(LANGUAGE_PARAM, language)
                .appendQueryParameter(ADULT_PARAM, inclAdult)
                .build();

        String resultStr = dataProcessing(builtUri);
        return resultStr;
    }

    public static String getDetailsData(String apiKey, String language, String movieId, String queryType) {
        return dataProcessing(builtUri(apiKey, language, movieId, queryType));

    }

    public static Uri builtUri(String apiKey, String language, String movieId, String queryType) {
        final String MOVIE_BASE_URL =
                "https://api.themoviedb.org/3/movie/" + movieId + "/" + queryType;
        final String APIKEY_PARAM = "api_key";
        final String LANGUAGE_PARAM = "language";

        Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                .appendQueryParameter(APIKEY_PARAM, apiKey)
                .appendQueryParameter(LANGUAGE_PARAM, language)
                .build();
        return builtUri;
    }

    public static String dataProcessing(Uri builtUri) {
        HttpURLConnection urlConnection = null;
        String movieJsonStr = null;
        BufferedReader reader = null;

        try {
            URL url = new URL(builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            movieJsonStr = buffer.toString();
        } catch (IOException e) {
            Log.e("Error", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("Error", "Error closing stream", e);
                }
            }
        }

        // This will only happen if there was an error getting or parsing the forecast.
        return movieJsonStr;
    }
}