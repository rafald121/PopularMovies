package com.example.admin.myapplication.Activities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.admin.myapplication.Adapters.MovieAdapter;
import com.example.admin.myapplication.Model.Movie;
import com.example.admin.myapplication.R;
import com.example.admin.myapplication.Utils.NetworkHelper;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = MainActivity.class.getSimpleName();
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
    private static final String ERROR = "ERROR";

    private String SELECTED_TYPE = null;

// jackson oject mapper json

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


    private void setupRecyclerView(RecyclerView recyclerView) {
        gridLayoutManager = new GridLayoutManager(MainActivity.this, 3);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

//        MenuItem refresh = menu.findItem(R.id.reload_data);
//        refresh.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (SELECTED_TYPE){
//                    case TOP_RATED:
//                        sortByTopRated();
//                        break;
//                    case MOST_POPULAR:
//                        sortByPopular();
//                        break;
//                    case ERROR:
//                        showErrorLayout("Check it ! ");
//                        break;
//                    default:
//                        Log.e(TAG, "onOptionsItemSelected: ERROR");
//                }
//                return true;
//            }
//        });

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
            case R.id.reload_data:
                switch (SELECTED_TYPE){
                    case TOP_RATED:
                        sortByTopRated();
                        break;
                    case MOST_POPULAR:
                        sortByPopular();
                        break;
                    case ERROR:
                        showErrorLayout("Check it ! ");
                        break;
                    default:
                        Log.e(TAG, "onOptionsItemSelected: ERROR");
                }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == refreshButton.getId())
            refreshData();
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

    class MovieQueryTask extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if(isNetworkAvailable()) {
                progressBar.setVisibility(View.VISIBLE);
                errorLayout.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
            } else {
                showErrorLayout("No inthernet connection");
                cancel(true);//abort
            }

        }

        @Override
        protected void onPostExecute(List<Movie> s) {

            progressBar.setVisibility(View.INVISIBLE);

            if (s == null) {
                showErrorLayout("Cannot fetch data from internet, list is null");
            } else {

                setActionBarTitle();

                recyclerView.setVisibility(View.VISIBLE);

                movieAdapter = new MovieAdapter(getApplicationContext(), s);
                recyclerView.setAdapter(movieAdapter);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected List<Movie> doInBackground(String... params) {

            if (params.length != 0 && !params[0].equals("") && params[0].length() != 0) {

                String urlType = params[0];
                String resultString = null;
                List<Movie> listOfMovies = new ArrayList<>();
                URL url = null;
                Uri uri;

                switch (urlType) {
                    case MOST_POPULAR:

                        uri = NetworkHelper.mostPopular();
                        url = NetworkHelper.buildURL(uri);

                        try {
                            resultString = NetworkHelper.getJsonDataFromResponse(url);
//                            TODO should I check in this place if resultString is not null? then go to next line
                            listOfMovies = NetworkHelper.convertJSONIntoList(resultString);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return listOfMovies;

                    case TOP_RATED:

                        uri = NetworkHelper.topRated();
                        url = NetworkHelper.buildURL(uri);

                        try {
                            resultString = NetworkHelper.getJsonDataFromResponse(url);
                            listOfMovies = NetworkHelper.convertJSONIntoList(resultString);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        return listOfMovies;
                }


            } else {
                showErrorLayout("Bad syntax of URL");
            }

            return null;
        }

    }

    private void showErrorLayout(String message) {

        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        errorLayout.setVisibility(View.VISIBLE);
        setActionBarTitle();
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
            case ERROR:
                title = getResources().getString(R.string.actionbar_title_most_popular);
                actionBar.setTitle(title);
                break;
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


//    private void setRecyclerViewVisible(RecyclerView recyclerview){
//        recyclerview.setVisibility(View.VISIBLE);
//    }
//
//    private void setRecyclerViewInvisible(RecyclerView recyclerview){
//        recyclerview.setVisibility(View.INVISIBLE);
//    }
//
//    private void setProgressBarVisible(ProgressBar progressbar){
//        progressbar.setVisibility(View.VISIBLE);
//    }
//
//    private void setProgressBarInvisible(ProgressBar progressbar){
//        progressbar.setVisibility(View.INVISIBLE);
//    }
//
//    private void setErrorLayoutVisible(LinearLayout linearlayout){
//        linearlayout.setVisibility(View.VISIBLE);
//    }
//
//    private void setErrorLayoutInvisible(LinearLayout linearlayout){
//        linearlayout.setVisibility(View.INVISIBLE);
//    }
//

}