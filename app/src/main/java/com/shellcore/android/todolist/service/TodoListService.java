package com.shellcore.android.todolist.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.shellcore.android.todolist.data.TodoListContract;

/**
 * Created by Cesar on 20/06/2017.
 */

public class TodoListService extends IntentService {

    public static final String EXTRA_TASK_DESCRIPTION = "extra_task_description";
    public static final String DATE = "date";

    public TodoListService() {
        super("TodoListService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String taskDesciption = intent.getStringExtra(EXTRA_TASK_DESCRIPTION);
        String date = intent.getStringExtra(DATE);

        ContentValues contentValues = new ContentValues();
        contentValues.put(TodoListContract.TodoEntry.COLUMN_DESCRIPTION, taskDesciption);
        contentValues.put(TodoListContract.TodoEntry.COLUMN_DATE, date);

        getContentResolver().insert(TodoListContract.TodoEntry.CONTENT_URI, contentValues);
    }
}
