package com.example.admin.myapplication.Activities;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.admin.myapplication.Adapters.MovieAdapter;
import com.example.admin.myapplication.Model.Movie;
import com.example.admin.myapplication.R;
import com.example.admin.myapplication.Utils.NetworkHelper;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
//    private RecyclerView recyclerView = null;
//    private MovieAdapter movieAdapter = null;

    private static final String TOP_RATED = "TOP_RATED";
    private static final String MOST_POPULAR = "MOST_POPULAR";


// jackson oject mapper json

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        sortByPopular();
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
            case R.id.sortByPopular:
                sortByPopular();
                return true;
            case R.id.sortByTopRated:
                sortByTopRated();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void sortByTopRated() {

        new MovieQueryTask().execute(TOP_RATED);

    }

    private void sortByPopular() {

        new MovieQueryTask().execute(MOST_POPULAR);

    }

    class MovieQueryTask extends AsyncTask<String, Void, List<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<Movie> s) {
            super.onPostExecute(s);

            for(Movie m:s)
                m.toString();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected List<Movie> doInBackground(String... params) {
            Log.i(TAG, "doInBackground: " + params[0]);
            
            if( params.length != 0 && !params[0].equals("") && params[0].length()!=0){

                String urlType = params[0];
                String resultString = null;
                List<Movie> listOfMovies = new ArrayList<>();
                URL url = null;
                Uri uri;

                switch (urlType){
                    case MOST_POPULAR:

                        uri = NetworkHelper.mostPopular();
                        url = NetworkHelper.buildURL(uri);

                        try {
                            Log.i(TAG, "doInBackground: before resultString");
                            resultString = NetworkHelper.getJsonDataFromResponse(url);

                            Log.i(TAG, "doInBackground: resultString: " + resultString);

                            listOfMovies = NetworkHelper.convertJSONIntoList(resultString);

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        for(Movie m  : listOfMovies)
                            Log.i(TAG, "doInBackground: " + m.toString());


                        
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
                        Log.i(TAG, "doInBackground: list" + listOfMovies.toString());


                        return listOfMovies;

                    default:
                        Log.e(TAG, "doInBackground: ERROR");
                        return null;

                }


            } else{
//            TODO ERROR
            }

            return null;
        }

    }


}
