package com.shellcore.android.todolist.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.shellcore.android.todolist.R;

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
        builder.setView(R.layout.add_task_dialog);
        builder.setTitle("Add a new task");
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }
}
