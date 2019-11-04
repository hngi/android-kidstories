package com.project.android_kidstories.data.source.local.preferences;

import android.app.Application;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

import static com.project.android_kidstories.data.source.local.preferences.PreferenceKeys.*;


public final class SharePref {
    private static SharePref INSTANCE;
    private SharedPreferences sharedPreferences;

    private SharePref(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public static synchronized SharePref getInstance(Application application) {
        if (INSTANCE == null) {
            INSTANCE = new SharePref(PreferenceManager.getDefaultSharedPreferences(application));
        }
        return INSTANCE;
    }


    public boolean getNightMode() {
        return sharedPreferences.getBoolean(IS_NIGHT_MODE, false);
    }

    public void setNightMode(boolean data) {
        sharedPreferences.edit()
                .putBoolean(IS_NIGHT_MODE, data)
                .apply();
    }

    public void setString(String key, String data) {
        sharedPreferences.edit().putString(key, data).apply();
    }

    public void setInt(String key, int data) {
        sharedPreferences.edit().putInt(key, data).apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    public int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }


    // Cached User Accessors
    public void saveLoginDetails(String token, String fullname, String email) {
        sharedPreferences.edit()
                .putString(CACHED_USER_TOKEN, token)
                .putString(CACHED_USER_NAME, fullname)
                .putString(CACHED_USER_EMAIL, email)
                .apply();
    }

    public String getUserToken() {
        return sharedPreferences.getString(CACHED_USER_TOKEN, null);
    }

    public String getUserFullname() {
        return sharedPreferences.getString(CACHED_USER_NAME, null);
    }

    public String getUserEmail() {
        return sharedPreferences.getString(CACHED_USER_EMAIL, null);
    }

    public int getUserId() {
        return sharedPreferences.getInt(CACHED_USER_ID, -1);
    }

    public void setUserId(Integer id) {
        sharedPreferences.edit().putInt(CACHED_USER_ID, id).apply();
    }

    public Long getLoggedUserId() {
        return sharedPreferences.getLong(KEY_ID, -1);
    }

    public Boolean getIsUserLoggedIn() {
        return sharedPreferences.getBoolean(STATE_USER_LOGGED_IN, false);
    }

    public void setIsUserLoggedIn(Boolean isUserLoggedIn) {
        sharedPreferences.edit().putBoolean(STATE_USER_LOGGED_IN, isUserLoggedIn).apply();
    }

    public boolean getBoolean(String key) {

    }
}
