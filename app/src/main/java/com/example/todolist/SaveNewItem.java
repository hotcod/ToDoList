package com.example.todolist;

import android.app.Activity;
import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;


public class SaveNewItem extends DialogFragment {
    SQLiteDatabase control;
    String input_text, noteName;

    public SaveNewItem(SQLiteDatabase control, EditText input_text, EditText noteName) {
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
                .setTitle("Save note?")
                .setPositiveButton("OK", (dialog, which) -> {

                    new FullDatabase().full(dialogActivity, "insert", control
                            , input_text, noteName);

                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialogActivity.finish();
                })
                .create();
    }
}
