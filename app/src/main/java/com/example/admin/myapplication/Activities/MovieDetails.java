package com.example.admin.myapplication.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.admin.myapplication.ConstantValues;
import com.example.admin.myapplication.R;
import com.example.admin.myapplication.Utils.NetworkHelper;

public class MovieDetails extends AppCompatActivity {

    private static final String TAG = MovieDetails.class.getSimpleName();

    private ActionBar actionBar = null;

    private TextView textViewTitle, textViewReleaseDate, textViewVoteAvarage, textViewDetails = null;
    private ImageView imageViewPoster = null;
    private Intent intentMovieDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        textViewTitle = (TextView) findViewById(R.id.movie_title);
        textViewReleaseDate = (TextView) findViewById(R.id.movie_release_date);
        textViewVoteAvarage = (TextView) findViewById(R.id.movie_vote_avarage);
        textViewDetails = (TextView) findViewById(R.id.movie_details);
        imageViewPoster = (ImageView) findViewById(R.id.movie_poster);

        intentMovieDetails = getIntent();
        actionBar = getSupportActionBar();
        setActionBarTitle();

        bind();

        Log.i(TAG, "onCreate: textViewTitle: " + intentMovieDetails.getStringExtra("textViewTitle"));

    }

    private void bind() {
        String title = null;
        String releaseDate = null;
        String voteAvarage = null;
        String details = null;
        String posterLink = null;

        if(intentMovieDetails.hasExtra(ConstantValues.MOVIE_TITLE))
            title = intentMovieDetails.getStringExtra(ConstantValues.MOVIE_TITLE);

        if(intentMovieDetails.hasExtra(ConstantValues.MOVIE_RELEASE_DATE))
            releaseDate = intentMovieDetails.getStringExtra(ConstantValues.MOVIE_RELEASE_DATE);

        if(intentMovieDetails.hasExtra(ConstantValues.MOVIE_VOTE_AVARAGE))
            voteAvarage = intentMovieDetails.getStringExtra(ConstantValues.MOVIE_VOTE_AVARAGE);

        if(intentMovieDetails.hasExtra(ConstantValues.MOVIE_DETAILS))
            details = intentMovieDetails.getStringExtra(ConstantValues.MOVIE_DETAILS);

        if(intentMovieDetails.hasExtra(ConstantValues.MOVIE_POSTERS))
            posterLink = NetworkHelper.URL_PICTURE_BASE + intentMovieDetails.getStringExtra(ConstantValues.MOVIE_POSTERS);


        textViewTitle.setText(title);
        textViewReleaseDate.setText(releaseDate);
        textViewVoteAvarage.setText(voteAvarage);
        textViewDetails.setText(details);


        Log.i(TAG, "bind: image url : " + posterLink);

        Glide.with(MovieDetails.this).load(posterLink)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageViewPoster);

    }

    private void setActionBarTitle() {
        if(intentMovieDetails.hasExtra(ConstantValues.MOVIE_TITLE))
            actionBar.setTitle(intentMovieDetails.getStringExtra(ConstantValues.MOVIE_TITLE));
    }

}
