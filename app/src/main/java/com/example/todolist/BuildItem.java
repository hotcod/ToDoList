package com.example.todolist;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.Arrays;

public class BuildItem extends AppCompatActivity implements View.OnClickListener {
    //views
    private ImageButton setColor, text_color, t_size_choose, back_main;
    private ImageView color_indicator, text_indicator;
    private ConstraintLayout bottom_sheet;
    private MaterialSpinner spinner;
    private CoordinatorLayout mainLayout;

    //   constants
    private final String[] SIZES = {"8", "10", "12", "14", "16", "18",
            "20", "22", "24", "26", "28", "30", "32", "36", "40", "44", "48", "52", "56", "60", "64"};
    public final SQLiteDatabase control = MainActivity.control;

    //other variables
    private BottomSheetBehavior<ConstraintLayout> bottom_sheet_behavior;
    private Intent intent;

    //statics
    //for fillings item in ListView
    public EditText input_text, noteName;
    public static int background;
    public static int colorText;
    public static int size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_item);

        setColor = findViewById(R.id.setColor);
        text_color = findViewById(R.id.text_color);
        color_indicator = findViewById(R.id.color_indicator);
        text_indicator = findViewById(R.id.text_indicator);
        input_text = findViewById(R.id.input_text);
        mainLayout = findViewById(R.id.mainLayout);
        bottom_sheet = findViewById(R.id.bottom_sheet);
        spinner = findViewById(R.id.size_spinner);
        t_size_choose = findViewById(R.id.t_size_choose);
        back_main = findViewById(R.id.back_main);
        noteName = findViewById(R.id.noteName);
        background = getResources().getColor(R.color.myElipTran);
        colorText = getResources().getColor(R.color.default_text);
        size = 16;
        bottom_sheet_behavior = BottomSheetBehavior.from(bottom_sheet);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onStart() {
        super.onStart();

        back_main.setOnClickListener(this);
        setColor.setOnClickListener(this);
        text_color.setOnClickListener(this);
        t_size_choose.setOnClickListener(this);

        spinner.setItems(SIZES);
        spinner.setSelectedIndex(4);
        spinner.setOnItemSelectedListener((view, position, id, item) -> {
            int set = Integer.parseInt((String) item);
            input_text.setTextSize(set);
            size = set;
            if (set != 16)
                t_size_choose.setBackground(AppCompatResources
                        .getDrawable(this, R.drawable.ic_text_size_chosen));
            else
                t_size_choose.setBackground(AppCompatResources
                        .getDrawable(this, R.drawable.ic_text_size_choose));
        });

        noteName.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 50) {
                    Toast.makeText(BuildItem.this
                            , getResources()
                                    .getString(R.string.max_characters),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        intent = this.getIntent();
        Cursor dataForItem = null;
        try {
            if (intent.getIdentifier().equals("fillingsItem")) {

                int id_data = intent.getIntExtra("id_cursor", 0);
                dataForItem = control.rawQuery("SELECT * FROM itemForList WHERE _id == ? ",
                        new String[]{String.valueOf(id_data)});

                if (dataForItem.moveToFirst()) {
                    background = dataForItem.getInt(dataForItem.getColumnIndex("background"));
                    colorText = dataForItem.getInt(dataForItem.getColumnIndex("colorText"));
                    size = dataForItem.getInt(dataForItem.getColumnIndex("size"));
                    input_text.setText(dataForItem.getString(dataForItem.getColumnIndex("text")));
                    noteName.setText(dataForItem.getString(dataForItem.getColumnIndex("noteName")));

                    mainLayout.setBackgroundColor(background);
                    if (background != R.color.myElipTran) {
                        setColor.setBackground(AppCompatResources
                                .getDrawable(this, R.drawable.ic_back_color_chosen));
                        color_indicator.setColorFilter(background, PorterDuff.Mode.MULTIPLY);
                    }

                    if (colorText != R.color.default_text) {
                        text_color.setBackground(AppCompatResources
                                .getDrawable(this, R.drawable.ic_text_color_chosen));
                        text_indicator.setColorFilter(colorText, PorterDuff.Mode.MULTIPLY);
                        input_text.setTextColor(colorText);
                    }
                    if (size != 16) {
                        input_text.setTextSize(size);
                        t_size_choose.setBackground(AppCompatResources
                                .getDrawable(this, R.drawable.ic_text_size_chosen));//check different between appcompat and getResources
                        spinner.setSelectedIndex(Arrays.asList(SIZES)
                                .indexOf(String.valueOf(size)));
                    }
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } finally {
            if (dataForItem != null) {
                dataForItem.close();
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.setColor || viewId == R.id.text_color) {
            ColorPickerDialogBuilder
                    .with(this)
                    .setTitle("Choose color")
                    .initialColor(Color.RED)
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setOnColorSelectedListener(selectedColor -> {
                    })
                    .setPositiveButton("ok", (dialog, selectedColor, allColors) -> {

                        if (v.getId() == R.id.setColor) {
                            color_indicator.setColorFilter(selectedColor
                                    , PorterDuff.Mode.MULTIPLY);
                            mainLayout.setBackgroundColor(selectedColor);
                            background = selectedColor;
                            if (selectedColor != Color.WHITE) {
                                setColor.setBackground(AppCompatResources.getDrawable
                                        (this, R.drawable.ic_back_color_chosen));
                            } else {
                                setColor.setBackground(AppCompatResources.getDrawable
                                        (this, R.drawable.ic_back_color_choose));
                            }
                        } else if (v.getId() == R.id.text_color) {
                            text_indicator.setColorFilter(selectedColor
                                    , PorterDuff.Mode.MULTIPLY);
                            input_text.setTextColor(selectedColor);
                            colorText = selectedColor;
                            if (selectedColor != R.color.default_text) {
                                text_color.setBackground(AppCompatResources.getDrawable
                                        (this, R.drawable.ic_text_color_chosen));

                            } else {
                                text_color.setBackground(AppCompatResources.getDrawable
                                        (this, R.drawable.ic_text_color_choose));
                                text_indicator.setColorFilter(Color.WHITE
                                        , PorterDuff.Mode.MULTIPLY);
                            }
                        }
                    })
                    .setNegativeButton("cancel", (dialog, which) -> {
                    })
                    .build()
                    .show();
        } else if (viewId == R.id.back_main) {
            try {
                if (intent.getIdentifier().equals("fillingsItem")) {
                    SaveEditItem dialog = new SaveEditItem(intent
                            .getIntExtra("id_cursor", -1), control
                            , input_text, noteName);
                    dialog.show(getSupportFragmentManager(), "edit");
                }
            } catch (NullPointerException e) {
                SaveNewItem dialog = new SaveNewItem(control, input_text, noteName);
                dialog.show(getSupportFragmentManager(), "save");
            }
        } else if (viewId == R.id.t_size_choose) {
            t_size_choose.setBackground(AppCompatResources
                    .getDrawable(this, R.drawable.ic_text_size_choose));
            spinner.setSelectedIndex(4);
            input_text.setTextSize(Float.parseFloat(SIZES[spinner.getSelectedIndex()]));
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (bottom_sheet_behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {

                Rect outRect = new Rect();
                bottom_sheet.getGlobalVisibleRect(outRect);

                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY()))
                    bottom_sheet_behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }

        return super.dispatchTouchEvent(event);
    }
}

