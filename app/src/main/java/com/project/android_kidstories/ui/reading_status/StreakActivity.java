package com.project.android_kidstories.ui.reading_status;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.project.android_kidstories.R;
import com.project.android_kidstories.sharePref.SharePref;

public class StreakActivity extends AppCompatActivity {

    public static final String STORIES_READ_KEY = "STORIES_READ_KEY";

    private static final String[] STATUSES = {"Beryl", "Emerald", "Ruby", "Diamond", "Jadeite"};
    private static final String[] STATUS_COLORS = {"#FFFF8F00", "#FF1565C0", "#FFAD1457", "#FFC62828", "#FF00BFA5"};

    @SuppressLint("SetTextI18n")
    private static void showStatus(Context context, String readingStatus, int color, int streak) {

        Dialog dialog = new Dialog(context, R.style.AppTheme_Dialog);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_streak_count);

        TextView txt = dialog.findViewById(R.id.txt_streak_count);
        txt.setText(streak + " stories read, ride on!");

        TextView txtStatus = dialog.findViewById(R.id.txt_reading_status);
        txtStatus.setText(readingStatus);

        View root = dialog.findViewById(R.id.rellay_reading_status);
        root.setBackgroundColor(color);

        dialog.show();
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, StreakActivity.class));
    }

    public static void displayUserReadingStatus(Context context, int storiesRead) {
        String status;
        String colorStr;

        if (storiesRead <= 10) {
            status = STATUSES[0];
            colorStr = STATUS_COLORS[0];
        } else if (storiesRead <= 20) {
            status = STATUSES[1];
            colorStr = STATUS_COLORS[1];
        } else if (storiesRead <= 30) {
            status = STATUSES[2];
            colorStr = STATUS_COLORS[2];
        } else if (storiesRead <= 40) {
            status = STATUSES[3];
            colorStr = STATUS_COLORS[3];
        } else {
            status = STATUSES[4];
            colorStr = STATUS_COLORS[4];
        }

        int color = Color.parseColor(colorStr);
        showStatus(context, status, color, storiesRead);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streak);

        TextView txt_reading_status = findViewById(R.id.txt_reading_status);
        int storiesRead = SharePref.getINSTANCE(this).getInt(STORIES_READ_KEY);

        String status;

        if (storiesRead <= 10) {
            status = STATUSES[0];
        } else if (storiesRead <= 20) {
            status = STATUSES[1];
        } else if (storiesRead <= 30) {
            status = STATUSES[2];
        } else if (storiesRead <= 40) {
            status = STATUSES[3];
        } else {
            status = STATUSES[4];
        }

        txt_reading_status.setText(status);
    }
}
