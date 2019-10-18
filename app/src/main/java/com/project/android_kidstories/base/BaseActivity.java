package com.project.android_kidstories.base;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.project.android_kidstories.LoginActivity;
import com.project.android_kidstories.Model.User;
import com.project.android_kidstories.R;
import com.project.android_kidstories.RegisterActivity;
import com.project.android_kidstories.Views.main.MainActivity;
import com.project.android_kidstories.sharePref.SharePref;

import java.util.HashMap;
import java.util.Map;

public class BaseActivity extends AppCompatActivity {
    private SharePref sharePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setThemes();
        super.onCreate(savedInstanceState);
        sharePref=SharePref.getINSTANCE(this);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener((sharedPreferences, key) -> {
//            Log.d(TAG, "key" + key);
            Map<String, ?> map = new HashMap<>();
            map = sharedPreferences.getAll();
//            Log.d(TAG, "map = " + map);
            Boolean night = (Boolean) map.get("NIGHT MODE");


//            if (night) {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
////                setTheme(R.style.DarkTheme);
////                Log.d(TAG, "Night mode is set");
//            } else {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
////                Log.d(TAG, "Night mode is unset");
////                setTheme(R.style.AppTheme);
//            }


        });
    }

    public SharePref getSharePref() {
        return sharePref;
    }

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    protected void openLoginActivity(Context context, User currentUser){
        Intent intent = new Intent(context, LoginActivity.class);

    }

    protected void openMainActivity(Context context, User currentUser) {
        Intent intent = new Intent(context, MainActivity.class);
        //intent.putExtra(MainActivity.USER_INTENT_EXTRA, currentUser);
        startActivity(intent);
    }

    protected void gotoRegisterActivity(Context context) {
        startActivity(new Intent(context, RegisterActivity.class));
    }

    protected void gotoLoginActivity(Context context) {
        startActivity(new Intent(context, LoginActivity.class));
    }
    public void setThemes(){
//        if (SharePref.getINSTANCE(this).getNightMode())
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {

            setTheme(R.style.DarkTheme);
        }
        else setTheme(R.style.AppTheme);
    }



}
