package com.project.android_kidstories.ui;

import android.app.Application;
import android.util.Log;
import androidx.appcompat.app.AppCompatDelegate;
import com.project.android_kidstories.data.source.local.preferences.SharePref;

public class KidstoriesApplication extends Application {
    private static final String TAG = "Common";


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: Common");

        SharePref sharePref = SharePref.getInstance(this);
        if (sharePref.getNightMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
