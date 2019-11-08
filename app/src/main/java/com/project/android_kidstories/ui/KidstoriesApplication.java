package com.project.android_kidstories.ui;

import android.app.Application;
import android.util.Log;
import androidx.appcompat.app.AppCompatDelegate;
import com.pixplicity.easyprefs.library.Prefs;
import com.project.android_kidstories.data.source.local.preferences.SharePref;

public class KidstoriesApplication extends Application {
    private static final String TAG = "Common";

    public static void updateSharedPref(int storyId, boolean value) {
        Prefs.putBoolean(String.valueOf(storyId), value);
    }

    public static void changeMode(boolean isNightMode) {
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: Common");

        SharePref sharePref = SharePref.getInstance(this);
        changeMode(sharePref.getNightMode());
    }

}
