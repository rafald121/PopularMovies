package com.example.admin.myapplication.Model;

/**
 * Created by admin on 01.04.2017.
 */

public class Movie  {

    private String idFromDBMovie;
    private String title;
    private String releaseDate;
    private String moviePoster;
    private String voteAvarage;
    private String details;

    public Movie(String idFromDBMovie, String title, String releaseDate, String moviePoster, String voteAvarage, String details) {
        this.idFromDBMovie = idFromDBMovie;
        this.title = title;
        this.releaseDate = releaseDate;
        this.moviePoster = moviePoster;
        this.voteAvarage = voteAvarage;
        this.details = details;
    }

    public String getIdFromDBMovie() {
        return idFromDBMovie;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public String getVoteAvarage() {
        return voteAvarage;
    }

    public String getDetails() {
        return details;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "idFromNet='" + idFromDBMovie + '\'' +
                ", title='" + title + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", moviePoster='" + moviePoster + '\'' +
                ", voteAvarage='" + voteAvarage + '\'' +
                ", details='" + details + '\'' +
                '}';
    }
}
