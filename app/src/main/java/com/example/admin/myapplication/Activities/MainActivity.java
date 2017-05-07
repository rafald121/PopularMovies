package com.example.admin.myapplication.Activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.myapplication.Adapters.MovieAdapter;
import com.example.admin.myapplication.ConstantValues.ConstantValues;
import com.example.admin.myapplication.Database.MovieDbConstant;
import com.example.admin.myapplication.Interfaces.RecyclerItemClickListener;
import com.example.admin.myapplication.JSONUtilities.MovieDetailsJSONParser;
import com.example.admin.myapplication.Model.Movie;
import com.example.admin.myapplication.R;
import com.example.admin.myapplication.Settings.SettingsActivity;
import com.example.admin.myapplication.Utils.NetworkHelper;
import com.example.admin.myapplication.Utils.Utils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//TODO UDACITY:
// /*
//  czy lepiej pobierać w Main Activity tylko id i obrazek a w details całą reszte( jeśli klikniemy) czy od razu całe obiekty juz w MainActivity
// */


public class MainActivity extends AppCompatActivity implements View.OnClickListener, RecyclerItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String[] MOVIE_DATABASE_COLUMNS = {
            MovieDbConstant.MovieEntries.COLUMN_TITLE,
            MovieDbConstant.MovieEntries.COLUMN_RELEASE_DATE,
            MovieDbConstant.MovieEntries.COLUMN_VOTE_AVARAGE,
            MovieDbConstant.MovieEntries.COLUMN_PLOT_SYNOPSIS,
            MovieDbConstant.MovieEntries.COLUMN_IMAGE_LINK,
            MovieDbConstant.MovieEntries.COLUMN_ID_FROM_NET
    };
    public static final int INDEX_COLUMN_TITLE = 0;
    public static final int  INDEX_COLUMN_RELEASE_DATE = 1;
    public static final int INDEX_COLUMN_VOTE_AVARAGE = 2;
    public static final int INDEX_COLUMN_PLOT_SYNOPSIS = 3;
    public static final int INDEX_COLUMN_IMAGE_LINK = 4;
    public static final int INDEX_COLUMN_ID_FROM_NET = 5;

    private static final String TOP_RATED = "TOP_RATED";
    private static final String MOST_POPULAR = "MOST_POPULAR";
    private static final String FAVOURITE = "FAVOURITE";
    private static String SELECTED_TYPE = MOST_POPULAR;//default

    private List<Movie> listOfMovies = new ArrayList<>();
    private RecyclerView recyclerView = null;
    private MovieAdapter movieAdapter = null;
    private GridLayoutManager gridLayoutManager;

    private ProgressBar progressBar = null;
    private LinearLayout errorLayout = null;
    private TextView errorMessage = null;

    private Button refreshButton = null;

    private ActionBar actionBar = null;

    private Parcelable recyclerViewPositionState;
    private Parcelable listOfMovieParcelabled;
    private int previousRecyclerViewPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.w(TAG, "onCreate: poczatek" );

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        progressBar = (ProgressBar) findViewById(R.id.progressbar_bar);

        errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        errorMessage = (TextView) findViewById(R.id.error_message);
        refreshButton = (Button) findViewById(R.id.refresh_button_error);
        refreshButton.setOnClickListener(this);

        actionBar = getSupportActionBar();

        if(Utils.isPhoneRotated(this))
            gridLayoutManager = new GridLayoutManager(MainActivity.this, 3);
        else
            gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);

        movieAdapter = new MovieAdapter(this, listOfMovies, MainActivity.this);
        recyclerView.setAdapter(movieAdapter);

        if(savedInstanceState==null)
            showListOfMovies();
        else
            Log.i(TAG, "onCreate: it isn't first application run so I doesn't download list of movies every time");

    }


    private void showListOfMovies() {
        switch (SELECTED_TYPE){
            case FAVOURITE:
                sortByFavourite();
                break;
            case TOP_RATED:
                sortByTopRated();
                break;
            case MOST_POPULAR:
                sortByPopular();
                break;
            default:
                Log.e(TAG, "showListOfMovies: error");
        }

    }

    private void sortByTopRated() {

        recyclerView.setVisibility(View.INVISIBLE);
        errorLayout.setVisibility(View.INVISIBLE);

        SELECTED_TYPE = TOP_RATED;
        new MovieQueryTask().execute(TOP_RATED);

    }
    private void sortByPopular() {

        recyclerView.setVisibility(View.INVISIBLE);
        errorLayout.setVisibility(View.INVISIBLE);

        SELECTED_TYPE = MOST_POPULAR;
        new MovieQueryTask().execute(MOST_POPULAR);

    }
    private void sortByFavourite() {

        recyclerView.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.INVISIBLE);

        SELECTED_TYPE = FAVOURITE;
        setActionBarTitle();

        Cursor cursor = getContentResolver().query(MovieDbConstant.MovieEntries.CONTENT_URI,
                null,
                null,
                null,
                null);

        if(cursor.getCount() != 0){
            populateRecycler(cursor);
        }
        else if(cursor.getCount() == 0){
            Log.i(TAG, "sortByFavourite: error");
        }

    }

    private void populateRecycler(Cursor cursor) {
        cursor.moveToFirst();

        listOfMovies = new ArrayList<>();

        do {
            Movie movie;

            String title = cursor.getString(cursor.getColumnIndex(MOVIE_DATABASE_COLUMNS[INDEX_COLUMN_TITLE]));
            String voteAvarage = cursor.getString(cursor.getColumnIndex(MOVIE_DATABASE_COLUMNS[INDEX_COLUMN_VOTE_AVARAGE]));
            String release = cursor.getString(cursor.getColumnIndex(MOVIE_DATABASE_COLUMNS[INDEX_COLUMN_RELEASE_DATE]));
            String plotSynopsis = cursor.getString(cursor.getColumnIndex(MOVIE_DATABASE_COLUMNS[INDEX_COLUMN_PLOT_SYNOPSIS]));
            String idFromNet = cursor.getString(cursor.getColumnIndex(MOVIE_DATABASE_COLUMNS[INDEX_COLUMN_ID_FROM_NET]));
            String imageLink = cursor.getString(cursor.getColumnIndex(MOVIE_DATABASE_COLUMNS[INDEX_COLUMN_IMAGE_LINK]));

            movie = new Movie(idFromNet, title, release, imageLink, voteAvarage, plotSynopsis);
            listOfMovies.add(movie);

        } while (cursor.moveToNext());

        movieAdapter = new MovieAdapter(getApplicationContext(), listOfMovies, MainActivity.this);
        recyclerView.setAdapter(movieAdapter);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int selectedItem = item.getItemId();

        switch (selectedItem) {
            case R.id.sort_by_popular:
                sortByPopular();
                return true;

            case R.id.sort_by_top_rated:
                sortByTopRated();
                return true;

            case R.id.sort_by_favourite:
                sortByFavourite();
                return true;

            case R.id.reload_data:
                switch (SELECTED_TYPE){
                    case TOP_RATED:
                        sortByTopRated();
                        break;
                    case MOST_POPULAR:
                        sortByPopular();
                        break;
                    case FAVOURITE:
                        sortByFavourite();
                        break;
                    default:
                        Log.e(TAG, "onOptionsItemSelected: ERROR");
                }
                break;

            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;


        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == refreshButton.getId())
            refreshData();
    }
    @Override
    public void onListItemClick(int clickedItemPosition) {
        Log.i(TAG, "onListItemClick: clickedItemPosition: " + clickedItemPosition);

        Intent intentMovieDetails;

        if(NetworkHelper.isNetworkAvailable(getApplicationContext())) {
            String idFromMovieDB = listOfMovies.get(clickedItemPosition).getIdFromDBMovie();

            intentMovieDetails = new Intent(MainActivity.this, MovieDetails.class);
            intentMovieDetails.putExtra(ConstantValues.IS_CONNECTION_AVAILABLE, true);
            intentMovieDetails.putExtra(ConstantValues.MOVIE_ID_FROM_NET, idFromMovieDB);

            startActivity(intentMovieDetails);
        } else{ // no connection

            if(SELECTED_TYPE.equals(FAVOURITE)){

                String idFromMovieDB = listOfMovies.get(clickedItemPosition).getIdFromDBMovie();

                intentMovieDetails = new Intent(MainActivity.this, MovieDetails.class);
                intentMovieDetails.putExtra(ConstantValues.IS_CONNECTION_AVAILABLE, false);
                intentMovieDetails.putExtra(ConstantValues.MOVIE_ID_FROM_NET, idFromMovieDB);

                startActivity(intentMovieDetails);

            } else if(SELECTED_TYPE.equals(TOP_RATED) || SELECTED_TYPE.equals(MOST_POPULAR)){
                Toast.makeText(this, "You can't see details from not favourite movie", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Log.e(TAG, "onListItemClick: bad error");
            }
        }
    }
    private void refreshData() {

        switch (SELECTED_TYPE) {
            case TOP_RATED:
                sortByTopRated();
                break;
            case MOST_POPULAR:
                sortByPopular();
                break;
            default:
                Log.e(TAG, "ERROR");
        }
    }
    private void showErrorLayout(String message) {

        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        errorLayout.setVisibility(View.VISIBLE);

        errorMessage.setText(message);

    }
    private void setActionBarTitle() {
        String title ;

        switch (SELECTED_TYPE) {
            case TOP_RATED:
                title = getResources().getString(R.string.actionbar_title_top_rated);
                actionBar.setTitle(title);
                break;
            case MOST_POPULAR:
                title = getResources().getString(R.string.actionbar_title_most_popular);
                actionBar.setTitle(title);
                break;
            case FAVOURITE:
                title = getResources().getString(R.string.actionbar_title_favourite);
                actionBar.setTitle(title);
            default:
                Log.e(TAG, "setActionBarTitle: ERROR");
        }

    }


    //TODO UDACITY how to separate AsyncTask class from here \/ to separate class (not inner) when this class (AsyncTask) use methods from MainActivity(for example, when OnPostExecute call - put data from list to recyclerview
    public class MovieQueryTask extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            setActionBarTitle();

            if(NetworkHelper.isNetworkAvailable(getApplicationContext())) {
                progressBar.setVisibility(View.VISIBLE);
                errorLayout.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
            } else {
                showErrorLayout(getResources().getString(R.string.error_message_no_connection));
                cancel(true);//abort
            }

        }

        @Override
        protected List<Movie> doInBackground(String... params) {
            Log.w(TAG, "doInBackground: " );
            if (params.length != 0 && !params[0].equals("") && params[0].length() != 0) {

                String urlType = params[0];
                String resultString;
                listOfMovies = new ArrayList<>();
                URL url = null;
                Uri uri;

                switch (urlType) {
                    case MOST_POPULAR:

                        uri = NetworkHelper.getUriMostPopular();
                        url = NetworkHelper.buildURL(uri);

                        try {
                            resultString = NetworkHelper.getJsonDataFromResponse(url);
                            listOfMovies = MovieDetailsJSONParser.convertJSONIntoMovieList(resultString);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return listOfMovies;

                    case TOP_RATED:

                        uri = NetworkHelper.getUriTopRated();
                        url = NetworkHelper.buildURL(uri);

                        try {
                            resultString = NetworkHelper.getJsonDataFromResponse(url);
                            listOfMovies = MovieDetailsJSONParser.convertJSONIntoMovieList(resultString);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return listOfMovies;
                }


            } else {
                showErrorLayout(getResources().getString(R.string.error_message_bad_url_syntax));
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> movieList) {
            Log.w(TAG, "onPostExecute: ");

            progressBar.setVisibility(View.INVISIBLE);

            if (movieList == null) {
                showErrorLayout(getResources().getString(R.string.error_message_list_is_null));
            } else {

//                if(recyclerViewPositionState!=null)
                    if(recyclerView.getLayoutManager().equals(gridLayoutManager))
                        if(recyclerView.getLayoutManager() != null) {
                            movieAdapter.swapData(movieList);
                            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewPositionState);
                            recyclerView.getLayoutManager().scrollToPosition(movieList.size()-1);
                        }
                        else
                            Log.i(TAG, "onPostExecute: recyclerView.getLayoutManager() == null");
                    else
                        Log.i(TAG, "onPostExecute: recycler.manager != gridlayoutmanager");
//                else
//                    Log.i(TAG, "onPostExecute: recyclerViewPositionState  == null");

                setActionBarTitle();
                recyclerView.setVisibility(View.VISIBLE);

            }
        }


    }

    protected void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        Log.w(TAG, "onSaveInstanceState: ");
        // Save list state
        if(recyclerView.getLayoutManager().equals(gridLayoutManager)){
            previousRecyclerViewPosition = gridLayoutManager.findLastCompletelyVisibleItemPosition();
            Log.i(TAG, "onSaveInstanceState: equal . Last visible element: " + previousRecyclerViewPosition);
        }
        else {
            Log.i(TAG, "onSaveInstanceState: not equal");
        }


        recyclerViewPositionState = recyclerView.getLayoutManager().onSaveInstanceState();
        state.putParcelableArrayList(ConstantValues.LIST_PARCELABLE, (ArrayList<? extends Parcelable>) listOfMovies);
        state.putParcelable(ConstantValues.RECYCLERVIEW_POSITION_STATE, recyclerViewPositionState);
        state.putInt(ConstantValues.RECYCLERVIEW_POSITION_INT, previousRecyclerViewPosition);

    }

    protected void onRestoreInstanceState(Bundle state) {
        super.onRestoreInstanceState(state);
        Log.w(TAG, "onRestoreInstanceState: ");
        if(state != null) {
            recyclerViewPositionState = state.getParcelable(ConstantValues.RECYCLERVIEW_POSITION_STATE);
            listOfMovies = state.getParcelableArrayList(ConstantValues.LIST_PARCELABLE);
            previousRecyclerViewPosition = state.getInt(ConstantValues.RECYCLERVIEW_POSITION_INT);
        }
        else
            Log.e(TAG, "onRestoreInstanceState: CAN STATE BE NULL ??? ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w(TAG, "onResume: " );

        if(recyclerView.getLayoutManager().equals(gridLayoutManager)){
            Log.i(TAG, "onResume: equal. Last visible element: " + previousRecyclerViewPosition);
        } else {
            Log.i(TAG, "onResume: not equal ");
        }

        if (recyclerViewPositionState != null) {
            movieAdapter.swapData(listOfMovies);

            recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewPositionState);
            recyclerView.getLayoutManager().scrollToPosition(previousRecyclerViewPosition);

            gridLayoutManager.onRestoreInstanceState(recyclerViewPositionState);
            gridLayoutManager.scrollToPosition(previousRecyclerViewPosition);

            //TODO it doesn't work even with recycler view delay like below
//            recyclerView.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    recyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewPositionState);
//                    recyclerView.getLayoutManager().scrollToPosition(previousRecyclerViewPosition);
//                }
//            }, 1500);
        }else {
            Log.e(TAG, "onResume: recycler position state is null when app start very first time " );
        }
    }


}