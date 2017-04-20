package com.example.admin.myapplication.Utils;

import android.net.Uri;
import android.util.Log;

import com.example.admin.myapplication.Model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 01.04.2017.
 */
//http://api.themoviedb.org/3/movie/321612/reviews?api_key=6e339219779d415f85a8fb48b3a9a07b

public class NetworkHelper {
    private static final String TAG = NetworkHelper.class.getSimpleName();

    private static final String URL_BASE = "http://api.themoviedb.org/3/movie";
    public static final String URL_PICTURE_BASE = "http://image.tmdb.org/t/p/w185";

    private static final String URL_TOP_RATED = "top_rated";
    private static final String URL_MOST_POPULAR = "popular";

    private static final String URL_KEY = "?api_key=6e339219779d415f85a8fb48b3a9a07b";

    public static Uri getUriMostPopular(){
        return Uri.parse(URL_BASE).buildUpon()
                .appendEncodedPath(URL_MOST_POPULAR)
                .appendEncodedPath(URL_KEY)
                .build();
    }

    public static Uri getUriTopRated(){
        return Uri.parse(URL_BASE).buildUpon()
                .appendEncodedPath(URL_TOP_RATED)
                .appendEncodedPath(URL_KEY)
                .build();
    }

    public static Uri getUriMovieDetail(String id){
        return Uri.parse(URL_BASE).buildUpon()
                .appendEncodedPath(id)
                .appendEncodedPath(URL_KEY)
                .build();
    }

    public static URL buildURL(Uri uri){

        URL url = null;

        try{
            url = new URL(uri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }

        return url;
    }


    public static String getJsonDataFromResponse(URL url) throws IOException {

        if(url == null && url.toString().equals("") && url.toString().length() == 0)
            return null;

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultString;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if(inputStream == null){
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;


            while((line = reader.readLine()) != null){
                buffer.append(line);
            }

            resultString = buffer.toString();

            return resultString;

        } catch (IOException e) {
            Log.e(TAG, "Error ", e);
            return null;

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }

    }


}
