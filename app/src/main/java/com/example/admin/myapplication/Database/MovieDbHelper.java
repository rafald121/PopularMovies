package com.example.admin.myapplication.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.admin.myapplication.Database.MovieDbConstant.MovieEntries;

/**
 * Created by admin on 19.04.2017.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movieDatabase";
    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntries.TABLE_NAME + " (" +

                MovieEntries.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieEntries.COLUMN_TITLE + " TEXT, " +
                MovieEntries.COLUMN_RELEASE_DATE + " TEXT, " +
                MovieEntries.COLUMN_VOTE_AVARAGE + " REAL NOT NULL, " +
                MovieEntries.COLUMN_PLOT_SYNOPSIS + " TEXT, " + ")";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntries.TABLE_NAME);
        onCreate(db);
    }
}
