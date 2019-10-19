package com.project.android_kidstories.Utils;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;

import com.google.gson.Gson;
import com.pixplicity.easyprefs.library.Prefs;
import com.project.android_kidstories.Api.HelperClasses.AddStoryHelper;

import java.util.HashMap;
import java.util.Map;

public class Common extends Application {
    private static final String TAG = "Common";

    @Override
    public void onCreate() {

        super.onCreate();
        Log.d(TAG, "onCreate: Common");

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Map<String, ?> map1 = new HashMap<>();
        Gson gson = new Gson();
        map1 = prefs.getAll();
        Log.d(TAG, "map = " + map1);

        if (prefs.contains("NIGHT MODE")) {
            Boolean night1 = (Boolean) map1.get("NIGHT MODE");

            if (night1){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }else
            prefs.edit().putBoolean("NIGHT MODE", false).apply();



        prefs.registerOnSharedPreferenceChangeListener((sharedPreferences, key) -> {
            Log.d(TAG, "key" + key);
            Map<String, ?> map;
            map = sharedPreferences.getAll();
            Log.d(TAG, "map = " + map);
            String night = String.valueOf(map.get("NIGHT MODE"));


            if (night == "true") {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                Log.d(TAG, "Night mode is changed to set");
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                Log.d(TAG, "Night mode is unset");

            }

        });
    }


    public static boolean checkNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void saveToken(String token) {
        Prefs.putString(AddStoryHelper.TOKEN_KEY, token);
    }

    private String getSavedToken(String token) {
        return Prefs.getString(AddStoryHelper.TOKEN_KEY, "aTokenStringShouldBeHere");
    }
//


}
