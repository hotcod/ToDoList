package com.example.todolist;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    //views
    private ImageView plusItem;
    private ListView mainList;

    //constants
    private final Context CONTEXT = this;
    private final int EDIT_ITEM_REQUEST = 2;

    //database
    public static SQLiteDatabase control;
    public SQLiteOpenHelper database;

    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        plusItem = findViewById(R.id.plusItem);
        mainList = findViewById(R.id.mainList);
//change name database
        database = new Help(this, "list_data", null, 1);
        control = database.getReadableDatabase();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onStart() {
        super.onStart();


        cursor = control.rawQuery("SELECT  * FROM itemForList Order by _id desc", null);
        String[] from = new String[]{"background", "colorText", "text", "noteName", "nowdate"};
        int[] to = new int[]{R.id.back_theme, R.id.text_item, R.id.text_item, R.id.noteName_item, R.id.time};

        //other variables
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.item,
                cursor, from, to, CursorAdapter.FLAG_AUTO_REQUERY);
        adapter.setViewBinder(new MyAdapter());
        mainList.setAdapter(adapter);

        registerForContextMenu(mainList);
        plusItem.setOnClickListener(this);

        mainList.setOnItemClickListener((parent, view, position, id) -> {

            Intent intent = new Intent(CONTEXT, BuildItem.class);
            Cursor dataForItem = control.rawQuery("SELECT * FROM itemForList WHERE _id == ? ",
                    new String[]{String.valueOf(id)});

            if (dataForItem.moveToFirst()) {
                int id_cursor = dataForItem.getInt(dataForItem.getColumnIndex("_id"));

                intent.putExtra("id_cursor", id_cursor);
                intent.setIdentifier("fillingsItem");
                startActivityForResult(intent, EDIT_ITEM_REQUEST);
            }
            dataForItem.close();
        });

        mainList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        database.close();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.plusItem) {
            Intent newViewItem = new Intent(CONTEXT, BuildItem.class);
            int NEW_ITEM_REQUEST = 1;
            startActivityForResult(newViewItem, NEW_ITEM_REQUEST);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        int ID_DELETE_ITEM_CM = 1;
        menu.add(0, ID_DELETE_ITEM_CM, 0, getString(R.string.click_delete_cm));
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        long id_item = ((AdapterView.AdapterContextMenuInfo) item.getMenuInfo()).id;
        control.delete("itemForList", "_id = ?", new String[]{String.valueOf(id_item)});
        cursor.requery();
        return true;
    }

    class MyAdapter implements SimpleCursorAdapter.ViewBinder {

        @Override
        public boolean setViewValue(View view, Cursor cursor, int columnIndex) {

            if (view.getId() == R.id.back_theme) {
                Drawable item = AppCompatResources.getDrawable(MainActivity.this, R.drawable.item_style);

                if (cursor.getInt(columnIndex) == R.color.myElipTran) {
                    view.setBackground(item);
                    return true;
                } else if (item != null) {
                    item.setColorFilter(cursor.getInt(columnIndex), PorterDuff.Mode.SRC);
                    view.setBackground(item);
                    return true;
                }

            }

            if (cursor.getColumnName(columnIndex).equals("colorText")
                    && cursor.getInt(columnIndex) != R.color.default_text) {
                ((TextView) view).setTextColor(cursor.getInt(columnIndex));
                return true;
            }

            return false;
        }
    }
}

