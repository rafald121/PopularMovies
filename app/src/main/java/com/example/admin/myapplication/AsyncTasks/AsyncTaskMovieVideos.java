package com.example.admin.myapplication.AsyncTasks;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.example.admin.myapplication.JSONUtilities.MovieVideosJSONParser;
import com.example.admin.myapplication.Model.MovieVideo;
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

public class AsyncTaskMovieVideos {
//    extends AsyncTask<String, Void, List<MovieVideo>> {
//        private static final String TAG = AsyncTaskMovieVideos.class.getSimpleName();
//
//    private WeakReference<Context> contextRef;
//
//    public AsyncTaskMovieVideos(Context context) {
//        contextRef = new WeakReference<>(context);
//    }
//
//    @Override
//    protected List<MovieVideo> doInBackground(String... params) {
//
//        if(!Utils.validateParams(params)) {
//            Log.e(TAG, "doInBackground: ERROR");
//            return null;
//        }
//
//        List<MovieVideo> listOfMovieVideo = null;
//        String resultString;
//        String id = params[0];
//
//        Uri uri = NetworkHelper.getUriMovieVideos(id);
//        URL url = NetworkHelper.buildURL(uri);
//
//        try{
//            resultString = NetworkHelper.getJsonDataFromResponse(url);
//            listOfMovieVideo = MovieVideosJSONParser.convertJSONIntoMovieVideoList(resultString);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return listOfMovieVideo;
//    }
//    @Override
//    protected void onPostExecute(List<MovieVideo> movieVideos) {
//
//        if(movieVideos != null && movieVideos.size() > 0 )  {
//            populateVideoRecyclerView(movieVideos);
//        } else {
//            layoutVideos.setVisibility(View.INVISIBLE);
//            movieDetailsVideosError.setVisibility(View.VISIBLE);
//            movieDetailsVideosError.setText(getResources().getString(R.string.no_videos));
//        }
//        super.onPostExecute(movieVideos);
//    }
//
//
}