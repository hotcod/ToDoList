package com.example.todolist;

import android.app.Activity;
import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class SaveEditItem extends DialogFragment {
    SQLiteDatabase control;
    int row_id;
    String input_text, noteName;

    public SaveEditItem(int row_id,SQLiteDatabase control, EditText input_text, EditText noteName) {
        this.row_id = row_id;
        this.control = control;
        this.input_text = input_text.getText().toString();
        this.noteName = noteName.getText().toString();
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity dialogActivity = getActivity();
        assert dialogActivity != null;
        AlertDialog.Builder builder = new AlertDialog.Builder(dialogActivity);
        return builder
                .setTitle("Edit note?")
                .setPositiveButton("Edit", (dialog, which) -> {

                    new FullDatabase().full(dialogActivity, "replace", control, row_id
                            , input_text, noteName);

                })
                .setNegativeButton("Cancel", (dialog, which) -> dialogActivity.finish())
                .create();
    }
}
