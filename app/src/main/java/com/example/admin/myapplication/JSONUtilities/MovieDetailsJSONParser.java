package com.example.admin.myapplication.JSONUtilities;

import com.example.admin.myapplication.Model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 19.04.2017.
 */

public class MovieDetailsJSONParser {

    private final static String JSON_RESULT = "results";
    private final static String JSON_ID_FROM_NET = "id";
    private final static String JSON_ORIGINAL_TITLE = "original_title";
    private final static String JSON_RELEASE_DATE = "release_date";
    private final static String JSON_DETAILS = "overview";
    private final static String JSON_POPULARITY = "popularity";
    private final static String JSON_VOTE_AVARAGE = "vote_average";
    private final static String JSON_POSTER_PATH = "poster_path";
//TODO jeśli tylko lista to potrzebujemy tylko image i id filmu
    public static List<Movie> convertJSONIntoMovieList(String jsonString) throws JSONException {

        if(jsonString == null)
            return null;
        if( jsonString.equals("") && jsonString.length()==0)
            return null;


        List<Movie> listOfMovies = new ArrayList<>();

        JSONObject root = new JSONObject(jsonString);
        JSONArray moviesJSONArray = root.getJSONArray(JSON_RESULT);

        for( int i = 0 ; i < moviesJSONArray.length(); i++){

            JSONObject movieJSON = moviesJSONArray.getJSONObject(i);

            String idFromNet = movieJSON.getString(JSON_ID_FROM_NET);
            String title = movieJSON.getString(JSON_ORIGINAL_TITLE);
            String releaseDate = movieJSON.getString(JSON_RELEASE_DATE);
            String details = movieJSON.getString(JSON_DETAILS);
            String posterPath = movieJSON.getString(JSON_POSTER_PATH);
            String voteAvarage = movieJSON.getString(JSON_VOTE_AVARAGE);

            Movie movie = new Movie(idFromNet, title, releaseDate, posterPath, voteAvarage, details);

            listOfMovies.add(movie);

        }

        return listOfMovies;

    }

    public static Movie convertJSONIntoSingleMovie(String jsonString) throws JSONException {

        if(jsonString == null)
            return null;
        if( jsonString.equals("") && jsonString.length()==0)
            return null;

        JSONObject root = new JSONObject(jsonString);

        String idFromNet = root.getString(JSON_ID_FROM_NET);
        String title = root.getString(JSON_ORIGINAL_TITLE);
        String releaseDate = root.getString(JSON_RELEASE_DATE);
        String details = root.getString(JSON_DETAILS);
        String posterPath = root.getString(JSON_POSTER_PATH);
        String voteAvarage = root.getString(JSON_VOTE_AVARAGE);

        return new Movie(idFromNet, title, releaseDate, posterPath, voteAvarage, details);


    }

}
