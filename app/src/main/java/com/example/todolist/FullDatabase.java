package com.example.todolist;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * the command can accept "insert" - insert into the table, "replace" - replace the data in the table
 * row_id - id row that will change
 */
public class FullDatabase {
    public void full(Activity activity, String command, SQLiteDatabase control, int row_id
            , String input_text, String noteName) {

        SimpleDateFormat dF = new SimpleDateFormat("dd.MM.yyyy");
        String resultDate = dF.format(new Date());

        ContentValues cv = new ContentValues();
        cv.put("background", BuildItem.background);
        cv.put("colorText", BuildItem.colorText);
        cv.put("size", BuildItem.size);
        cv.put("text", input_text);
        cv.put("noteName", noteName);
        cv.put("nowdate", resultDate);
        cv.put("_id", row_id);

       if(command.equals("replace")) {
            control.replace("itemForList", null, cv);
        }

        activity.finish();
    }

    public void full(Activity activity, String command, SQLiteDatabase control
            , String input_text, String noteName) {

        SimpleDateFormat dF = new SimpleDateFormat("dd.MM.yyyy");
        String resultDate = dF.format(new Date());

        ContentValues cv = new ContentValues();
        cv.put("background", BuildItem.background);
        cv.put("colorText", BuildItem.colorText);
        cv.put("size", BuildItem.size);
        cv.put("text", input_text);
        cv.put("noteName", noteName);
        cv.put("nowdate", resultDate);

        if (command.equals("insert")) {
            control.insert("itemForList", null, cv);
        }

        activity.finish();
    }
}
