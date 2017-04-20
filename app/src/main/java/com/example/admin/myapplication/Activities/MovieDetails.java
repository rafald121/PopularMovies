package com.example.admin.myapplication.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.admin.myapplication.ConstantValues.ConstantValues;
import com.example.admin.myapplication.JSONUtilities.MovieDetailsJSONParser;
import com.example.admin.myapplication.JSONUtilities.MovieReviewsJSONParser;
import com.example.admin.myapplication.JSONUtilities.MovieVideosJSONParser;
import com.example.admin.myapplication.Model.Movie;
import com.example.admin.myapplication.Model.MovieReview;
import com.example.admin.myapplication.Model.MovieVideo;
import com.example.admin.myapplication.R;
import com.example.admin.myapplication.Utils.NetworkHelper;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetails extends AppCompatActivity
//        implements  LoaderManager.LoaderCallbacks<MovieDetails>
{
    private static final String TAG = MovieDetails.class.getSimpleName();

    @BindView(R.id.movie_title)         TextView textViewTitle;
    @BindView(R.id.movie_release_date)  TextView textViewReleaseDate;
    @BindView(R.id.movie_vote_avarage)  TextView textViewVoteAvarage;
    @BindView(R.id.movie_details)       TextView textViewDetails;
    @BindView(R.id.movie_poster)        ImageView imageViewPoster;

    @BindView(R.id.movieReview)         TextView reviewwwTEMP;

    private ActionBar actionBar = null;

    private Intent intentMovieDetails;

    private String movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);
        actionBar = getSupportActionBar();

        ButterKnife.bind(this);

        intentMovieDetails = getIntent();
        if(intentMovieDetails.hasExtra(ConstantValues.MOVIE_ID_FROM_NET)) {
            movieId = intentMovieDetails.getStringExtra(ConstantValues.MOVIE_ID_FROM_NET);
        } else {
            //TODO zrob komunikat jesli nie pobierze ID
        }

        new AsyncTaskMovieDetail().execute(movieId);
        new AsyncTaskMovieReviews().execute(movieId);
        new AsyncTaskMovieVideos().execute(movieId);
    }

    private void bind(Movie movie) {

        String title = movie.getTitle();
        String releaseDate = movie.getReleaseDate();
        String voteAvarage = movie.getVoteAvarage();
        String details = movie.getDetails();
        String posterLink = movie.getMoviePoster();

        textViewTitle.setText(title);
        textViewReleaseDate.setText(releaseDate);
        textViewVoteAvarage.setText(voteAvarage);
        textViewDetails.setText(details);

        Glide.with(MovieDetails.this).load(posterLink)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageViewPoster);

    }

    private void setActionBarTitle(String title) {
        actionBar.setTitle(title);
    }


    public class AsyncTaskMovieDetail extends AsyncTask<String, Void, Movie>{

        private final String TAG_AT_MovieDetails = AsyncTaskMovieDetail.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            //TODO DODAC LOADERA jakeis koleczko
            super.onPreExecute();
        }

        @Override
        protected Movie doInBackground(String... params) {

            if(!validateParams(params)){
//TODO ZRob cos
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
            if(movie == null){
                Log.i(TAG, "onPostExecute: movie is null");
            }
            bind(movie);
            setActionBarTitle(movie.getTitle());
        }
    }

    public class AsyncTaskMovieReviews extends AsyncTask<String, Void, List<MovieReview>>{

        private final String TAG_AT_MovieReviews = AsyncTaskMovieReviews.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            //TODO DODAC LOADERA jakeis koleczko
            super.onPreExecute();
        }

        @Override
        protected List<MovieReview> doInBackground(String... params) {

            if(!validateParams(params)){
                //TODO zrob cos
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
            if(movieReview!=null)
                Log.i(TAG_AT_MovieReviews, "onPostExecute: " + movieReview.toString());
            else
                Log.i(TAG, "onPostExecute: list is null");

            super.onPostExecute(movieReview);
        }
    }

    public class AsyncTaskMovieVideos extends AsyncTask<String, Void, List<MovieVideo>> {

        private final String TAG_AT_MovieVideos = AsyncTaskMovieVideos.class.getSimpleName();

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected List<MovieVideo> doInBackground(String... params) {

            if(!validateParams(params)) {
                //TODO zrob cos
                return null;
            }

            List<MovieVideo> listOfMovieVideo = null;
            String resultString;
            String id = params[0];

            Uri uri = NetworkHelper.getUriMovieVideos(id);
            URL url = NetworkHelper.buildURL(uri);

            try{
                resultString = NetworkHelper.getJsonDataFromResponse(url);
                listOfMovieVideo = MovieVideosJSONParser.convertJSONIntoMovieVideoList(resultString);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return listOfMovieVideo;
        }

        @Override
        protected void onPostExecute(List<MovieVideo> movieVideos) {
            if(movieVideos == null) {
                //TODO zabezpieczyc
                Log.i(TAG, "onPostExecute: list is null");
            } else {

                Log.i(TAG, "onPostExecute: git");
            }
            super.onPostExecute(movieVideos);
        }
    }

    private boolean validateParams(String[] params){
        if(params == null || params[0] == null){
            return false;
        }

        if(params.length==0 && params[0].equals("")){
            return false;
        }

        if(params[0].length() == 0) {
            return false;
        }

        return true;
    }

    //TODO UDACITY czy moze byc cos innego niz Cursor w Loaderze \/
//    @Override
//    public Loader<MovieDetails> onCreateLoader(int id, Bundle args) {
//        switch (id){
//            case MOVIE_DETAIL_ID:
//                Uri uri = NetworkHelper.getUriMovieDetail(movieId);
//                URL url = NetworkHelper.buildURL(uri);
//
//
//
//
//        }
//    }
//
//    @Override
//    public void onLoadFinished(Loader<MovieDetails> loader, MovieDetails data) {
//
//    }
//
//    @Override
//    public void onLoaderReset(Loader<MovieDetails> loader) {
//
//    }

}
