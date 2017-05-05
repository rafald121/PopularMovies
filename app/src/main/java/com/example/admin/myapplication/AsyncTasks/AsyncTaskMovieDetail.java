package com.example.admin.myapplication.AsyncTasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.admin.myapplication.Interfaces.AsyncTaskMovieDetailListener;
import com.example.admin.myapplication.Interfaces.AsyncTaskMovieVideosListener;
import com.example.admin.myapplication.JSONUtilities.MovieDetailsJSONParser;
import com.example.admin.myapplication.Model.Movie;
import com.example.admin.myapplication.Utils.NetworkHelper;
import com.example.admin.myapplication.Utils.Utils;

import org.json.JSONException;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;

/**
 * Created by admin on 05.05.2017.
 */

public class AsyncTaskMovieDetail extends AsyncTask<String, Void, Movie> {

    private static final String TAG = AsyncTaskMovieDetail.class.getSimpleName();
    private AsyncTaskMovieDetailListener listener;
    private WeakReference<Context> contextRef;

    public AsyncTaskMovieDetail(Context context) {
        contextRef = new WeakReference<>(context);
        listener = (AsyncTaskMovieDetailListener) contextRef;
    }

    @Override
    protected Movie doInBackground(String... params) {

        if(!Utils.validateParams(params)){
            Log.e(TAG, "doInBackground: ERROR");
            return null;
        }

        Movie movieDetails = null;
        String resultString;
        String id = params[0];

        Uri uri = NetworkHelper.getUriMovieDetail(id);
        URL url = NetworkHelper.buildURL(uri);
        try{
            resultString = NetworkHelper.getJsonDataFromResponse(url);
            movieDetails = MovieDetailsJSONParser.convertJSONIntoSingleMovie(resultString);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return movieDetails;

    }

    @Override
    protected void onPostExecute(Movie movie) {
        if(movie != null){
           listener.detailsOnPostExecuteSuccess(movie);
        } else{
            listener.detailsOnPostExecuteFailure(movie);
        }
    }

}
