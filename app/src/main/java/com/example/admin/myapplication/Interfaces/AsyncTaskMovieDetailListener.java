package com.example.admin.myapplication.Interfaces;

import com.example.admin.myapplication.Model.Movie;
import com.example.admin.myapplication.Model.MovieReview;

import java.util.List;

/**
 * Created by admin on 05.05.2017.
 */

public interface AsyncTaskMovieDetailListener {
    void detailsOnPostExecuteSuccess(Movie movie);
    void detailsOnPostExecuteFailure(Movie movie);
}
