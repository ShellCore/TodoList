package com.shellcore.android.todolist.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shellcore.android.todolist.R;
import com.shellcore.android.todolist.service.TodoListService;

import java.util.Calendar;

/**
 * Created by Cesar on 20/06/2017.
 */

public class AddTaskDialogFragment extends DialogFragment {

    public static AddTaskDialogFragment getInstance(String idTask) {
        Bundle bundle = new Bundle();
        bundle.putString("ID_TASK", idTask);
        AddTaskDialogFragment fragment = new AddTaskDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.add_task_dialog, null, false);
        final EditText edtTask = (EditText) view.findViewById(R.id.edt_task);
        builder.setView(view);
        builder.setTitle("Add a new task");
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(getContext(), TodoListService.class);
                intent.putExtra(TodoListService.EXTRA_TASK_DESCRIPTION, edtTask.getText().toString());
                intent.putExtra(TodoListService.DATE, Calendar.getInstance().getTime().toString());
                getActivity().startService(intent);

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }
}
