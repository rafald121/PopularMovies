package com.example.admin.myapplication.Model;

/**
 * Created by admin on 01.04.2017.
 */

public class Movie  {

    private String title;
    private String releaseDate;
    private String moviePoster;
    private String voteAvarage;
    private String popularity;
    private String details;

    public Movie(String title, String releaseDate, String moviePoster, String voteAvarage, String popularity, String details) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.moviePoster = moviePoster;
        this.voteAvarage = voteAvarage;
        this.popularity = popularity;
        this.details = details;
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

    public String getRate() {
        return popularity;
    }

    public String getDetails() {
        return details;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", moviePoster='" + moviePoster + '\'' +
                ", voteAvarage='" + voteAvarage + '\'' +
                ", rate='" + popularity + '\'' +
                ", details='" + details + '\'' +
                '}';
    }
}
