package com.project.android_kidstories.data.source.local.preferences;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

public final class SharePref {

    private static final String LAST_LOGGED_IN ="LAST_LOGGED_IN";
    private static final String ID_KEY="com.project.android_kidstories_ID_KEY";
    private static final String USER_LOGIN_STATE = "isUserLoggedIn";
    private static final String NIGHT_MODE = "NIGHT MODE";

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
        return sharedPreferences.getBoolean(NIGHT_MODE, false);
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

    public void saveLoginDetails(String token, String firstname, String lastname, String email) {
        sharedPreferences = application.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Token", token);
        editor.putString("Firstname", firstname);
        editor.putString("Lastname", lastname);
        editor.putString("Email", email);
        editor.apply();
    }

    public String getUserToken() {
        sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("Token", "");
    }

    public String getUserFirstname() {
        sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("Firstname", "");
    }

    public String getUserLastname() {
        sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("Lastname", "");
    }

    public String getUserEmail() {
        sharedPreferences = context.getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        return sharedPreferences.getString("Email", "");
    }

    public Long getLoggedUserId(){
        return sharedPreferences.getLong(ID_KEY,-1);
    }

    public void setLoggedUserId(Long id){
        sharedPreferences.edit().putLong(ID_KEY,id).apply();
    }

    public Boolean getIsUserLoggedIn() {
        return sharedPreferences.getBoolean(USER_LOGIN_STATE, false);
    }

    public void setIsUserLoggedIn(Boolean isUserLoggedIn) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putBoolean(USER_LOGIN_STATE, isUserLoggedIn).apply();
    }

    public int getUserId() {
        return sharedPreferences.getInt("User Id", 0);
    }

    public void setUserId(Integer id){

        sharedPreferences.edit().putInt("User Id", id).apply();
    }
}
