package com.shellcore.android.todolist.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Cesar on 20/06/2017.
 */

public class TodoProvider extends ContentProvider {

    private static final int TODO = 100;
    private static final int TODO_ID = 101;

    // The Uri Matcher used by this content provider
    private static final UriMatcher uriMatcher = buildUriMatcher();
    private TodoListDBHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new TodoListDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.

        Cursor retCursor;
        switch (uriMatcher.match(uri)) {
            case TODO:
                retCursor = dbHelper.getReadableDatabase()
                        .query(
                                TodoListContract.TodoEntry.TABLE_NAME,
                                projection,
                                selection,
                                selectionArgs,
                                null,
                                null,
                                sortOrder
                        );
                break;
            case TODO_ID:
                retCursor = dbHelper.getReadableDatabase()
                        .query(
                                TodoListContract.TodoEntry.TABLE_NAME,
                                projection,
                                TodoListContract.TodoEntry._ID + " = '" + ContentUris.parseId(uri) + "'",
                                null,
                                null,
                                null,
                                sortOrder
                        );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case TODO:
                long _id = db.insert(TodoListContract.TodoEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = TodoListContract.TodoEntry.CONTENT_URI;
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver()
                .notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowSelected;
        switch (match) {
            case TODO:
                rowSelected = db.delete(
                        TodoListContract.TodoEntry.TABLE_NAME,
                        selection,
                        selectionArgs
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Because a null delete all the rows
        if (selection == null || rowSelected != 0) {
            getContext().getContentResolver()
                    .notifyChange(uri, null);
        }
        return rowSelected;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsUpdated;
        switch (match) {
            case TODO:
                rowsUpdated = db.update(
                        TodoListContract.TodoEntry.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Because a null delete all the rows
        if (rowsUpdated != 0) {
            getContext().getContentResolver()
                    .notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        switch (match) {
            case TODO:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for(ContentValues value : values) {
                        long _id = db.insert(TodoListContract.TodoEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver()
                        .notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }

    private static UriMatcher buildUriMatcher() {
        // All paths added to the UriMatcher have a corresponding code to return when a match is
        // found. The code passed into the constructor represents the code to return for the root
        // URI. It's common to use NOT_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = TodoListContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code
        matcher.addURI(authority, TodoListContract.PATH_TODO, TODO);
        matcher.addURI(authority, TodoListContract.PATH_TODO + "/#", TODO_ID);

        return matcher;
    }
}
