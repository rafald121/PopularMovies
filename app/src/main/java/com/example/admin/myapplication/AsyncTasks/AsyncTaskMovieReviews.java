package com.example.admin.myapplication.AsyncTasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.example.admin.myapplication.Activities.MovieDetails;
import com.example.admin.myapplication.Interfaces.AsyncTaskMovieVideosListener;
import com.example.admin.myapplication.Interfaces.AsyncTastMovieReviewListener;
import com.example.admin.myapplication.JSONUtilities.MovieReviewsJSONParser;
import com.example.admin.myapplication.Model.MovieReview;
import com.example.admin.myapplication.R;
import com.example.admin.myapplication.Utils.NetworkHelper;
import com.example.admin.myapplication.Utils.Utils;

import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.List;

/**
 * Created by admin on 05.05.2017.
 */

public class AsyncTaskMovieReviews extends AsyncTask<String, Void, List<MovieReview>> {

    private static final String TAG = AsyncTaskMovieReviews.class.getSimpleName();

    private AsyncTastMovieReviewListener listener;
    private WeakReference<Context> contextRef;

    public AsyncTaskMovieReviews(Context context, AsyncTastMovieReviewListener listener) {
        contextRef = new WeakReference<>(context);
        this.listener = listener;
    }

    @Override
    protected List<MovieReview> doInBackground(String... params) {

        if(!Utils.validateParams(params)){
            Log.e(TAG, "doInBackground: error");
            return null;
        }

        List<MovieReview> listOfMovieReviews = null;
        String resultString;
        String id = params[0];

        Uri uri = NetworkHelper.getUriMovieReviews(id);
        URL url = NetworkHelper.buildURL(uri);

        try{
            resultString = NetworkHelper.getJsonDataFromResponse(url);
            listOfMovieReviews = MovieReviewsJSONParser.convertJSONIntoList(resultString);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return listOfMovieReviews;
    }
    @Override
    protected void onPostExecute(List<MovieReview> movieReview) {
        if(movieReview!=null && movieReview.size() > 0){
            listener.reviewOnPostExecuteSuccess(movieReview);
        }
        else {
            listener.reviewOnPostExecuteFailure(movieReview);
        }

        super.onPostExecute(movieReview);
    }

}
