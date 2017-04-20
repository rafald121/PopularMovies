package com.example.admin.myapplication.JSONUtilities;

import com.example.admin.myapplication.Model.MovieVideo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 19.04.2017.
 */

public class MovieVideosJSONParser {

    private static final String RESULT = "result";
    private static final String MOVIE_ID = "movieId";
    private static final String MOVIE_KEY = "movieKey";
    private static final String MOVIE_NAME = "movieName";

    public static List<MovieVideo> convertJSONIntoMovieVideoList(String jsonString) throws JSONException{

        if(jsonString == null)
            return null;
        if( jsonString.equals("") && jsonString.length()==0)
            return null;

        List<MovieVideo> listOfMovieVideos = new ArrayList<>();

        JSONObject root = new JSONObject(jsonString);
        JSONArray movieVideoArray  = root.getJSONArray(RESULT);

        for (int i = 0; i < movieVideoArray.length()   ; i++) {

            JSONObject movieVideoJSON = movieVideoArray.getJSONObject(i);

            String id = movieVideoJSON.getString(MOVIE_ID);
            String key = movieVideoJSON.getString(MOVIE_KEY);
            String name = movieVideoJSON.getString(MOVIE_NAME);

            MovieVideo movieVideo = new MovieVideo(id,key,name);

            listOfMovieVideos.add(movieVideo);
        }

        return listOfMovieVideos;
    }



}
