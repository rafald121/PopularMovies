package com.example.admin.myapplication.Model;

/**
 * Created by admin on 20.04.2017.
 */

public class MovieVideo {

    private String YTname;
    private String id;
    private String YTkey;

    public MovieVideo(String id, String YTkey, String YTname) {
        this.YTname = YTname;
        this.id = id;
        this.YTkey = YTkey;
    }

    public String getYTname() {
        return YTname;
    }

    public String getIdFromDBMovie() {
        return id;
    }

    public String getYTkey() {
        return YTkey;
    }

    @Override
    public String toString() {
        return "MovieVideo{" +
                "YTkey='" + YTkey + '\'' +
                ", idFromDBMovie='" + id + '\'' +
                ", YTname='" + YTname + '\'' +
                '}';
    }
}
