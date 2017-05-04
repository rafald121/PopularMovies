package com.example.admin.myapplication.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
////http://img.youtube.com/vi/tWapqpCEO7Y/default.jpg
//https://www.youtube.com/watch?v=SF58Lsvqg5E
public class NetworkHelper {
    private static final String TAG = NetworkHelper.class.getSimpleName();

    private static final String URL_BASE = "http://api.themoviedb.org/3/movie";
    public static final String URL_PICTURE_BASE = "http://image.tmdb.org/t/p/w185";

    private static final String URL_TOP_RATED = "top_rated";
    private static final String URL_MOST_POPULAR = "popular";
    private static final String URL_REVIEWS = "reviews";
    private static final String URL_VIDEOS = "videos";

    private static final String YOUTUBE_VIDEO_BASE = "http://www.youtube.com/watch?v=";
    private static final String YOUTUBE_IMAGE_BASE = "http://img.youtube.com/vi";
    private static final String YOUTUBE_IMAGE_DEFAULT = "default.jpg";
    private static final String YOUTUBE_IMAGE_MEDIUM  = "mqdefault.jpg";

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

        String uriString = Uri.parse(URL_BASE).buildUpon()
                .appendEncodedPath(id)
                .build().toString();

        uriString = uriString + URL_KEY;

        return Uri.parse(uriString);
    }

    public static Uri getUriMovieReviews(String id) {

        String uriString = Uri.parse(URL_BASE).buildUpon()
                .appendEncodedPath(id)
                .appendEncodedPath(URL_REVIEWS)
                .build().toString();

        uriString += URL_KEY;

        return Uri.parse(uriString);

    }

    public static Uri getUriMovieVideo(String id){
       return Uri.parse(YOUTUBE_VIDEO_BASE + id);
    }

    public static Uri getUriMovieVideos(String id){
        String uriString = Uri.parse(URL_BASE).buildUpon()
                .appendEncodedPath(id)
                .appendEncodedPath(URL_VIDEOS)
                .build().toString();

        uriString += URL_KEY;

        return Uri.parse(uriString);

    }


//    public static Uri getUriMovieVideosImage(String id){
//        return Uri.parse("");
//    }

    public static Uri getUriMovieImage(String imageId){
        return Uri.parse(URL_PICTURE_BASE).buildUpon()
                .appendEncodedPath(imageId).build();
    }

    public static Uri getUriMovieVideoImage(String videoKey) {
        return Uri.parse(YOUTUBE_IMAGE_BASE).buildUpon()
                .appendEncodedPath(videoKey)
                .appendEncodedPath(YOUTUBE_IMAGE_MEDIUM).build();
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

    public static boolean isNetworkAvailable(Context applicationContext) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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
