package com.shellcore.android.todolist.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.shellcore.android.todolist.R;
import com.shellcore.android.todolist.data.TodoListContract;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Cesar on 20/06/2017.
 */

public class TodoCursorAdapter extends RecyclerView.Adapter<TodoCursorAdapter.ViewHolder> {

    private Cursor cursor;
    private ToggleTodoCheckListener listener;

    public TodoCursorAdapter(Cursor cursor, Context context) {
        this.cursor = cursor;
        try {
            listener = (ToggleTodoCheckListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ToggleTodoCheckListener");
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        cursor.moveToPosition(position);
        holder.bindView(cursor);
    }

    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    public void swapCursor(Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private int todoTaskId = -1;

        @BindView(R.id.chk_task)
        CheckBox chkTask;
        @BindView(R.id.txt_description)
        TextView txtDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(Cursor cursor) {
            todoTaskId = cursor.getInt(cursor.getColumnIndex(TodoListContract.TodoEntry._ID));
            final boolean isTaskDone = cursor.getInt(cursor.getColumnIndex(TodoListContract.TodoEntry.COLUMN_DONE)) == 1;
            String description = cursor.getString(cursor.getColumnIndex(TodoListContract.TodoEntry.COLUMN_DESCRIPTION));

            txtDescription.setText(description);
            toggleTask(isTaskDone);
            chkTask.setChecked(isTaskDone);

            chkTask.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    toggleTask(isChecked);
                    listener.onTodoItemChange(todoTaskId, isChecked);
                }
            });
        }

        private void toggleTask(boolean isTaskDone) {
            if (isTaskDone) {
                txtDescription.setPaintFlags(txtDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }else {
                txtDescription.setPaintFlags(txtDescription.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
        }
    }

    public interface ToggleTodoCheckListener {
        void onTodoItemChange(int todoID, boolean done);
    }
}
