package com.example.admin.myapplication.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by admin on 01.04.2017.
 */

public class Movie implements Parcelable {

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

    protected Movie(Parcel in) {
        idFromDBMovie = in.readString();
        title = in.readString();
        releaseDate = in.readString();
        moviePoster = in.readString();
        voteAvarage = in.readString();
        details = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idFromDBMovie);
        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeString(moviePoster);
        dest.writeString(voteAvarage);
        dest.writeString(details);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
