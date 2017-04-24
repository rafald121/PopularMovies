package com.example.admin.myapplication.JSONUtilities;

import com.example.admin.myapplication.Model.MovieReview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 19.04.2017.
 */

public class MovieReviewsJSONParser {

    private final static String JSON_RESULT = "results";
    private final static String REVIEW_ID = "id";
    private final static String REVIEW_AUTHOR = "author";
    private final static String REVIEW_CONTENT = "content";
    private final static String REVIEW_URL = "url";

    public static List<MovieReview> convertJSONIntoList(String jsonString) throws JSONException {

        if(jsonString == null)
            return null;
        if( jsonString.equals("") && jsonString.length()==0)
            return null;

        List<MovieReview> listOfMovieReviews = new ArrayList<>();

        JSONObject root = new JSONObject(jsonString);
        JSONArray reviewsArray = root.getJSONArray(JSON_RESULT);

        for( int i = 0 ; i < reviewsArray.length(); i++) {

            JSONObject reviewJSON = reviewsArray.getJSONObject(i);

            String id = reviewJSON.getString(REVIEW_ID);
            String author = reviewJSON.getString(REVIEW_AUTHOR);
            String content = reviewJSON.getString(REVIEW_CONTENT);
            String url = reviewJSON.getString(REVIEW_URL);

            MovieReview movieReview = new MovieReview(id, author, content, url);

            listOfMovieReviews.add(movieReview);
        }

        return listOfMovieReviews;
    }

}
