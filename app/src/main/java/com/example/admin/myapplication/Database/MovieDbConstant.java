package com.example.admin.myapplication.Database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by admin on 19.04.2017.
 */

public class MovieDbConstant {

    public static final String AUTHORITY = "com.example.admin.myapplication";

    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIE = "movie";

    public static final class MovieEntries implements BaseColumns{

        public static final Uri CONTENT_URI = BASE_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String TABLE_NAME = "movies";

        public static final String COLUMN_ID = "id";
        public static final String COLUMN_ID_FROM_NET = "idFromNet";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_VOTE_AVARAGE = "voteAvarage";
        public static final String COLUMN_PLOT_SYNOPSIS = "plotSynopsis";
        public static final String COLUMN_IMAGE_LINK = "imageLink";
        public static final String COLUMN_IMAGE = "image";

    }

}
