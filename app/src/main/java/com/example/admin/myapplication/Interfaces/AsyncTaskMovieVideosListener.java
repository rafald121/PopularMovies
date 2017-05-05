package com.example.admin.myapplication.Interfaces;

import com.example.admin.myapplication.Model.MovieVideo;

import java.util.List;

/**
 * Created by admin on 05.05.2017.
 */

public interface AsyncTaskMovieVideosListener {
    void videoOnPostExecuteSuccess(List<MovieVideo> movieVideosList);
    void videoOnPostExecuteFailure(List<MovieVideo> movieVideosList);
}
