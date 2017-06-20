package com.shellcore.android.todolist;

import android.app.LoaderManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.shellcore.android.todolist.adapter.TodoCursorAdapter;
import com.shellcore.android.todolist.data.TodoListContract;
import com.shellcore.android.todolist.dialogs.AddTaskDialogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, TodoCursorAdapter.ToggleTodoCheckListener {

    // Servicios
    private TodoCursorAdapter adapter;

    // Componentes
    @BindView(R.id.rec_main)
    RecyclerView recMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        adapter = new TodoCursorAdapter(null, this);

        setupRecyclerView();

        getLoaderManager().initLoader(0, null, this);
    }

    @OnClick(R.id.btn_add)
    public void onClickBtnAdd() {
        AddTaskDialogFragment dialogFragment = new AddTaskDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), "addTask");
    }

    private void setupRecyclerView() {
        recMain.setLayoutManager(new LinearLayoutManager(this));
        recMain.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recMain.setHasFixedSize(true);
        recMain.setAdapter(adapter);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, TodoListContract.TodoEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public void onTodoItemChange(int todoID, boolean done) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TodoListContract.TodoEntry.COLUMN_DONE, done ? 1 : 0);
        String[] mSelectionArgs = {Integer.toString(todoID)};
        getContentResolver().update(TodoListContract.TodoEntry.CONTENT_URI, contentValues, " _ID =? ", mSelectionArgs);
    }

    private void showNotification() {
        String message = BuildConfig.BASE_URL; // This String is defined in the build.gradle app file, from the flavor integration
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // NOTIFICATION_ID allows you to update the notification later on.
        notificationManager.notify(1, builder.build());

    }
}
