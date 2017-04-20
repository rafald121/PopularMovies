package com.example.admin.myapplication.Model;

/**
 * Created by admin on 20.04.2017.
 */

public class Review {

    private String idFromDBMovie;
    private String author;
    private String content;
    private String url;

    public Review(String idFromDBMovie, String author, String content, String url) {
        this.idFromDBMovie = idFromDBMovie;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    public String getIdFromDBMovie() {
        return idFromDBMovie;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public String toString() {
        return "Review{" +
                "idFromDBMovie='" + idFromDBMovie + '\'' +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}

