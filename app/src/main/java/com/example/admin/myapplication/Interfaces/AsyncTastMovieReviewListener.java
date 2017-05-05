package com.example.admin.myapplication.Interfaces;

import com.example.admin.myapplication.Model.MovieReview;
import com.example.admin.myapplication.Model.MovieVideo;

import java.util.List;

/**
 * Created by admin on 05.05.2017.
 */

public interface AsyncTastMovieReviewListener {
    void reviewOnPostExecuteSuccess(List<MovieReview> movieReviewList);
    void reviewOnPostExecuteFailure(List<MovieReview> movieReviewList);
}
