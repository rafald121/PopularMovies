package com.example.admin.myapplication.Activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.admin.myapplication.Adapters.MovieAdapter;
import com.example.admin.myapplication.ConstantValues.ConstantValues;
import com.example.admin.myapplication.Database.MovieDbConstant;
import com.example.admin.myapplication.Interfaces.RecyclerItemClickListener;
import com.example.admin.myapplication.JSONUtilities.MovieDetailsJSONParser;
import com.example.admin.myapplication.Model.Movie;
import com.example.admin.myapplication.R;
import com.example.admin.myapplication.Settings.SettingsActivity;
import com.example.admin.myapplication.Utils.NetworkHelper;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//TODO UDACITY:
// /*
//  czy lepiej pobierać w Main Activity tylko id i obrazek a w details całą reszte( jeśli klikniemy) czy od razu całe obiekty juz w MainActivity
// */


public class MainActivity extends AppCompatActivity implements View.OnClickListener, RecyclerItemClickListener,  {

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
    public static final int INDEX_COLUMN_RELEASE_DATE = 1;
    public static final int INDEX_COLUMN_VOTE_AVARAGE = 2;
    public static final int INDEX_COLUMN_PLOT_SYNOPSIS = 3;
    public static final int INDEX_COLUMN_IMAGE_LINK = 4;
    public static final int INDEX_COLUMN_ID_FROM_NET = 5;

    public static final int ID_MOVIE_LOADER = 41;

    private List<Movie> listOfMovies = null;

    private RecyclerView recyclerView = null;
    private MovieAdapter movieAdapter = null;
    private GridLayoutManager gridLayoutManager;
    private ProgressBar progressBar = null;

    private LinearLayout errorLayout = null;
    private TextView errorMessage = null;
    private Button refreshButton = null;

    private ActionBar actionBar = null;

    private static final String TOP_RATED = "TOP_RATED";
    private static final String MOST_POPULAR = "MOST_POPULAR";
    private static final String FAVOURITE = "FAVOURITE";

    private String SELECTED_TYPE = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        progressBar = (ProgressBar) findViewById(R.id.progressbar_bar);

        errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        errorMessage = (TextView) findViewById(R.id.error_message);
        refreshButton = (Button) findViewById(R.id.refresh_button_error);
        refreshButton.setOnClickListener(this);

        actionBar = getSupportActionBar();

        setupRecyclerView(recyclerView);

        sortByPopular();
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

        //TODO show movies from content provider with LoaderManager;
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

    private void setupRecyclerView(RecyclerView recyclerView) {
        gridLayoutManager = new GridLayoutManager(MainActivity.this, 5);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
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

        String idFromMovieDB = listOfMovies.get(clickedItemPosition).getIdFromDBMovie();

        Intent intentMovieDetails = new Intent(MainActivity.this, MovieDetails.class);
        intentMovieDetails.putExtra(ConstantValues.MOVIE_ID_FROM_NET, idFromMovieDB);

        startActivity(intentMovieDetails);
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
                title = getResources().getString(R.string.sort_by_favourite);
                actionBar.setTitle(title);
            default:
                Log.e(TAG, "setActionBarTitle: ERROR");
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

//TODO UDACITY jak oddzielic to od oddzielnej klasy jeśli w tej klasie \/ używamy metod z klasy MainActivity
    public class MovieQueryTask extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            setActionBarTitle();

            if(isNetworkAvailable()) {
                progressBar.setVisibility(View.VISIBLE);
                errorLayout.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
            } else {
                //TODO LOAD FROM DATABASE WHEN NETWORK IS NOT AVAILABLE
                //TODO is this appropriate to use Strings from reources in code, not in xml like line below?
                /*Yes, it is appropriate to use. In fact, it is a recommended practice, particularly if you want to localize the strings (i.e support different languages).
                Some helpful references on this:
                https://medium.com/google-developer-experts/android-strings-xml-things-to-remember-c155025bb8bb
                https://developer.android.com/guide/topics/resources/localization.html*/
                showErrorLayout(getResources().getString(R.string.error_message_no_connection));
                cancel(true);//abort
            }

        }

        @Override
        protected List<Movie> doInBackground(String... params) {

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
        protected void onPostExecute(List<Movie> s) {

            progressBar.setVisibility(View.INVISIBLE);

            if (s == null) {
                showErrorLayout(getResources().getString(R.string.error_message_list_is_null));
            } else {
                setActionBarTitle();
                recyclerView.setVisibility(View.VISIBLE);

                movieAdapter = new MovieAdapter(getApplicationContext(), s, MainActivity.this);
                recyclerView.setAdapter(movieAdapter);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }

}