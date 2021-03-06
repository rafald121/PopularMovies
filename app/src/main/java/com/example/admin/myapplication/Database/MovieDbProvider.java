package com.example.admin.myapplication.Database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.admin.myapplication.Database.MovieDbConstant.MovieEntries;

import static com.example.admin.myapplication.Database.MovieDbConstant.MovieEntries.TABLE_NAME;

/**
 * Created by admin on 19.04.2017.
 */

public class MovieDbProvider extends ContentProvider {

    private static final String TAG = MovieDbProvider.class.getSimpleName();

    public static final int CODE_MOVIE = 420;
    public static final int CODE_MOVIE_SINGLE = 421;

    private static final UriMatcher uriMatcher = buildUriMatcher();
    private MovieDbHelper movieDbHelper;

    public static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String movieAuthority = MovieDbConstant.AUTHORITY;

        matcher.addURI(movieAuthority, MovieDbConstant.PATH_MOVIE, CODE_MOVIE);
        matcher.addURI(movieAuthority, MovieDbConstant.PATH_MOVIE + "/#", CODE_MOVIE_SINGLE);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        movieDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor;

        switch (uriMatcher.match(uri)){
            case CODE_MOVIE:{
                cursor = movieDbHelper.getReadableDatabase().query(
                        TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case CODE_MOVIE_SINGLE:{

                String _selection = MovieEntries.COLUMN_ID + " = ? ";

                String idFromContentProvider = uri.getLastPathSegment();
                String[] selectionId = new String[]{idFromContentProvider};

                cursor = movieDbHelper.getReadableDatabase().query(
                        TABLE_NAME,
                        projection,
                        _selection,
                        selectionId,
                        null,
                        null,
                        sortOrder);
                break;

            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);
        Uri insertedUri = null;

        switch (match){
            case CODE_MOVIE:

                long id1 = db.insert(TABLE_NAME, null, values);

                if(id1>0){
                    insertedUri = ContentUris.withAppendedId(MovieDbConstant.MovieEntries.CONTENT_URI, id1);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }

                break;

            case CODE_MOVIE_SINGLE:{

                long id2 = db.insert(TABLE_NAME, null, values);

                if(id2>0){
                    insertedUri = ContentUris.withAppendedId(MovieDbConstant.MovieEntries.CONTENT_URI, id2);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;

            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }
        getContext().getContentResolver().notifyChange(uri, null);

        return insertedUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        int deletedTaskId = 0;

        switch (match){
            case CODE_MOVIE_SINGLE:

                String id = uri.getPathSegments().get(1);

                deletedTaskId = db.delete(TABLE_NAME, "id=?", new String[]{id});

                break;
            case CODE_MOVIE:
                Log.i(TAG, "delete: CODE MOVIE ");
                break;

            default:
                throw new UnsupportedOperationException("Uknown uri: " + uri);
        }

        if(deletedTaskId != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        } else{
            Log.i(TAG, "delete: error while deleting");
        }

        return deletedTaskId;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
