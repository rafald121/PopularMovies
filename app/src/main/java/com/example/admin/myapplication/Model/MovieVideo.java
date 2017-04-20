package com.example.admin.myapplication.Model;

/**
 * Created by admin on 20.04.2017.
 */

public class MovieVideo {

    private String YTname;
    private String idFromDBMovie;
    private String YTkey;

    public MovieVideo(String YTname, String idFromDBMovie, String YTkey) {
        this.YTname = YTname;
        this.idFromDBMovie = idFromDBMovie;
        this.YTkey = YTkey;
    }


    public String getYTname() {
        return YTname;
    }

    public String getIdFromDBMovie() {
        return idFromDBMovie;
    }

    public String getYTkey() {
        return YTkey;
    }

    @Override
    public String toString() {
        return "MovieVideo{" +
                "YTkey='" + YTkey + '\'' +
                ", idFromDBMovie='" + idFromDBMovie + '\'' +
                ", YTname='" + YTname + '\'' +
                '}';
    }
}
