package com.example.admin.myapplication.Activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.admin.myapplication.Adapters.MovieReviewAdapter;
import com.example.admin.myapplication.Adapters.MovieVideoAdapter;
import com.example.admin.myapplication.ConstantValues.ConstantValues;
import com.example.admin.myapplication.Database.MovieDbConstant;
import com.example.admin.myapplication.Interfaces.MovieVideoClickListener;
import com.example.admin.myapplication.JSONUtilities.MovieDetailsJSONParser;
import com.example.admin.myapplication.JSONUtilities.MovieReviewsJSONParser;
import com.example.admin.myapplication.JSONUtilities.MovieVideosJSONParser;
import com.example.admin.myapplication.Model.Movie;
import com.example.admin.myapplication.Model.MovieReview;
import com.example.admin.myapplication.Model.MovieVideo;
import com.example.admin.myapplication.R;
import com.example.admin.myapplication.Utils.NetworkHelper;
import com.example.admin.myapplication.Utils.Utils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetails extends AppCompatActivity implements MovieVideoClickListener{
    private static final String TAG = MovieDetails.class.getSimpleName();

    @BindView(R.id.movie_title)         TextView textViewTitle;
    @BindView(R.id.movie_release_date)  TextView textViewReleaseDate;
    @BindView(R.id.movie_vote_avarage)  TextView textViewVoteAvarage;
    @BindView(R.id.movie_details)       TextView textViewDetails;
    @BindView(R.id.movie_poster)        ImageView imageViewPoster;
    @BindView(R.id.recyclerview_review) RecyclerView recyclerViewReviews;
    @BindView(R.id.recyclerview_videos) RecyclerView recyclerViewVideos;
    @BindView(R.id.view_videos)         View layoutVideos;
    @BindView(R.id.view_reviews)        View reviewVideos;
    @BindView(R.id.movie_details_reviews_error) TextView movieDetailsReviewsError;
    @BindView(R.id.movie_details_videos_error)  TextView movieDetailsVideosError;


    public static final int ID_MOVIE_LOADER = 41;

    private MovieReviewAdapter movieReviewAdapter;
    private MovieVideoAdapter movieVideoAdapter;

    private Movie movieObj;
    private MenuItem menuItemFavourite;

    private ActionBar actionBar = null;

    private Intent intentMovieDetails;

    private String movieIdFromNet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);
        setDefaultViewsVisibility();

        intentMovieDetails = getIntent();
        if(intentMovieDetails.hasExtra(ConstantValues.IS_CONNECTION_AVAILABLE)){

            if(intentMovieDetails.getBooleanExtra(ConstantValues.IS_CONNECTION_AVAILABLE, false) == true){

                if(intentMovieDetails.hasExtra(ConstantValues.MOVIE_ID_FROM_NET)) {

                    movieIdFromNet = intentMovieDetails.getStringExtra(ConstantValues.MOVIE_ID_FROM_NET);

                    new AsyncTaskMovieDetail().execute(movieIdFromNet);
                    new AsyncTaskMovieReviews().execute(movieIdFromNet);
                    new AsyncTaskMovieVideos().execute(movieIdFromNet);

                } else {
                    Log.i(TAG, "onCreate: error tukej1");
                    //TODO zrob komunikat jesli nie pobierze ID
                }

            } else{

                if(intentMovieDetails.hasExtra(ConstantValues.MOVIE_ID_FROM_NET) == true){

                    movieIdFromNet = intentMovieDetails.getStringExtra(ConstantValues.MOVIE_ID_FROM_NET);
                    
                    getDetailsFromContentProvider(movieIdFromNet);

                } else {
                    Log.i(TAG, "onCreate: error tukej2");

                    //TODO zrob komunikat jesli nie pobierze ID
                }

            }

        } else{
            Log.e(TAG, "onCreate: bad error");
        }

    }

    private void setDefaultViewsVisibility() {
        movieDetailsReviewsError.setVisibility(View.INVISIBLE);
        movieDetailsVideosError.setVisibility(View.INVISIBLE);
    }

    private void getDetailsFromContentProvider(String movieIdFromNet) {

        long idFromContentProvider = -1;
        Uri uriToId = MovieDbConstant.MovieEntries.CONTENT_URI;

        Cursor cursor= getContentResolver().query(uriToId,
                new String[]{MovieDbConstant.MovieEntries.COLUMN_ID}, 
                MovieDbConstant.MovieEntries.COLUMN_ID_FROM_NET + " = ? ", 
                new String[]{movieIdFromNet}, 
                null );


        if(cursor.getCount()!=0) {
            cursor.moveToFirst();

            idFromContentProvider = cursor.getLong(cursor.getColumnIndex(MovieDbConstant.MovieEntries.COLUMN_ID));
        }     else {
            Log.e(TAG, "getDetailsFromContentProvider: ERROR");
        }
        //TODO hide videos and reviews when offline mode

        Uri uriToDetails = Uri.parse(String.valueOf(MovieDbConstant.MovieEntries.CONTENT_URI + "/" + idFromContentProvider));

        Cursor cursorWithDetails = getContentResolver().query(uriToDetails,
                null,
                null,
                null,
                null);


        if(cursorWithDetails.getCount()!=0) {
            cursor.moveToFirst();

            bind(cursorWithDetails);
        } else{
            Log.e(TAG, "getDetailsFromContentProvider: error");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details, menu);

        menuItemFavourite = menu.findItem(R.id.select_favourite);

        long id = isFavourite(movieIdFromNet);
        if(id != 0)
            menuItemFavourite.setIcon(R.drawable.ic_favorite_black_24dp);
        else if(id==0)
            menuItemFavourite.setIcon(R.drawable.ic_favorite_border_black_24dp);

        menuItemFavourite.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                long id = isFavourite(movieIdFromNet);

                if( id != 0 ){
                    deleteFromDatabase(id);
                    Toast.makeText(MovieDetails.this, "Movie has been remove from favourite!", Toast.LENGTH_SHORT).show();
                    item.setIcon(R.drawable.ic_favorite_border_black_24dp);
                } else {

//TODO addToDatabase available only on online mode

                    if(addToDatabase(movieIdFromNet)) {
                        //TODO udacity: how to set new movie id (from content provider) to item with this movie?
                        item.setIcon(R.drawable.ic_favorite_black_24dp);
                        Toast.makeText(MovieDetails.this, "Movie has been added to favourite!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MovieDetails.this, "Failed to add to favourite", Toast.LENGTH_SHORT).show();
                    }

                }
                return false;
            }
        });

        return true;
    }

    private boolean deleteFromDatabase(long id) {

        String sId = Long.toString(id);

        Uri uri = MovieDbConstant.MovieEntries.CONTENT_URI;
        uri = uri.buildUpon().appendPath(sId).build();
        Log.i(TAG, "deleteFromDatabase: uriDelete: " + uri.toString());
        getContentResolver().delete(uri, null, null);

        return false;
    }

    private boolean addToDatabase(String movieIdFromNet) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(MovieDbConstant.MovieEntries.COLUMN_ID_FROM_NET, movieIdFromNet);
        contentValues.put(MovieDbConstant.MovieEntries.COLUMN_TITLE, textViewTitle.getText().toString());
        contentValues.put(MovieDbConstant.MovieEntries.COLUMN_RELEASE_DATE, textViewReleaseDate.getText().toString());
        contentValues.put(MovieDbConstant.MovieEntries.COLUMN_VOTE_AVARAGE, textViewVoteAvarage.getText().toString());
        contentValues.put(MovieDbConstant.MovieEntries.COLUMN_PLOT_SYNOPSIS, textViewDetails.getText().toString());
        contentValues.put(MovieDbConstant.MovieEntries.COLUMN_IMAGE_LINK, movieObj.getMoviePoster());

        Uri uri = getContentResolver().insert(MovieDbConstant.MovieEntries.CONTENT_URI, contentValues);

        String lastSegmentFromUri = uri.getLastPathSegment();

        if (uri != null) {
            return true;
        }
        else {
            return false;
        }

    }

    private long isFavourite(String movieIdFromNet) {

        Cursor cursor;

        cursor = getContentResolver().query(MovieDbConstant.MovieEntries.CONTENT_URI,
                null,
                MovieDbConstant.MovieEntries.COLUMN_ID_FROM_NET + "=" + movieIdFromNet,
                null,
                null);

        if(cursor.getCount() == 0){ // nie jest favourite
            return 0;
        }
        else { // jest favourite
            cursor.moveToFirst();
            long id = cursor.getLong(cursor.getColumnIndex(MovieDbConstant.MovieEntries.COLUMN_ID));
            return id;
        }

    }

    private void setActionBarTitle(String title) {
        actionBar.setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void bind(Movie movie) {

        String title = movie.getTitle();
        String releaseDate = movie.getReleaseDate();
        String voteAvarage = movie.getVoteAvarage();
        String details = movie.getDetails();
        String posterLink = NetworkHelper.getUriMovieImage(movie.getMoviePoster()).toString();

        textViewTitle.setText(title);
        textViewReleaseDate.setText(releaseDate);
        textViewVoteAvarage.setText(voteAvarage);
        textViewDetails.setText(details);

        Glide.with(MovieDetails.this).load(posterLink)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageViewPoster);

    }

    private void bind(Cursor cursor){
        cursor.moveToFirst();

        String title = cursor.getString(cursor.getColumnIndex(MovieDbConstant.MovieEntries.COLUMN_TITLE));
        setActionBarTitle(title);

        textViewTitle.setText(title);
        textViewReleaseDate.setText(cursor.getString(cursor.getColumnIndex(MovieDbConstant.MovieEntries.COLUMN_RELEASE_DATE)));
        textViewVoteAvarage.setText(cursor.getString(cursor.getColumnIndex(MovieDbConstant.MovieEntries.COLUMN_VOTE_AVARAGE)));
        textViewDetails.setText(cursor.getString(cursor.getColumnIndex(MovieDbConstant.MovieEntries.COLUMN_PLOT_SYNOPSIS)));
    }


    private void populateReviewRecyclerView(List<MovieReview> movieReview) {
        movieReviewAdapter = new MovieReviewAdapter(movieReview, this);

        LinearLayoutManager recyclerManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewReviews.setLayoutManager(recyclerManager);
        recyclerViewReviews.setHasFixedSize(true);

        recyclerViewReviews.setAdapter(movieReviewAdapter);

    }

    private void populateVideoRecyclerView(List<MovieVideo> movieVideos) {
        movieVideoAdapter = new MovieVideoAdapter(movieVideos, this, this);

        LinearLayoutManager recyclerManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewVideos.setLayoutManager(recyclerManager);
        recyclerViewVideos.setHasFixedSize(true);

        recyclerViewVideos.setAdapter(movieVideoAdapter);

    }

    @Override
    public void onClickMovieVideoListener(String key) {
        Uri videoUri = NetworkHelper.getUriMovieVideo(key);
        Intent YTintent = new Intent(Intent.ACTION_VIEW, videoUri);
        startActivity(YTintent);
    }

    public class AsyncTaskMovieDetail extends AsyncTask<String, Void, Movie>{

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
                movieObj = movie;
                bind(movie);
                setActionBarTitle(movie.getTitle());
            } else{
                //TODO show error
                Log.i(TAG, "onPostExecute: movie is null");
                return;
            }
        }

    }

    public class AsyncTaskMovieReviews extends AsyncTask<String, Void, List<MovieReview>>{

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
                populateReviewRecyclerView(movieReview);
            }
            else {
                reviewVideos.setVisibility(View.INVISIBLE);
                movieDetailsReviewsError.setVisibility(View.VISIBLE);
                //                TODO POPRAWIC PUSTO
                movieDetailsReviewsError.setText("Pusto");
                Log.i(TAG, "onPostExecute: list is null");
            }

            super.onPostExecute(movieReview);
        }

    }

    public class AsyncTaskMovieVideos extends AsyncTask<String, Void, List<MovieVideo>> {

        @Override
        protected List<MovieVideo> doInBackground(String... params) {

            if(!Utils.validateParams(params)) {
                Log.e(TAG, "doInBackground: ERROR");
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

            if(movieVideos != null && movieVideos.size() > 0 )  {
                populateVideoRecyclerView(movieVideos);
            } else {
                layoutVideos.setVisibility(View.INVISIBLE);
                movieDetailsVideosError.setVisibility(View.VISIBLE);
//                TODO POPRAWIC TO PUSTO
                movieDetailsVideosError.setText("pusto");
            }
            super.onPostExecute(movieVideos);
        }

    }

    //TODO UDACITY czy moze byc cos innego niz Cursor w Loaderze \/

}
