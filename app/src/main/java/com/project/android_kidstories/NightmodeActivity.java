package com.project.android_kidstories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.project.android_kidstories.R;
import com.project.android_kidstories.base.BaseActivity;
import com.project.android_kidstories.sharePref.SharePref;

public class NightmodeActivity extends BaseActivity {

    //    This is supposed to change the view of the app from Light to Dark mode.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // getActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nightmode);

        Switch nightSwitch = findViewById(R.id.night_switch);

//AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        if (getSharePref().getNightMode()) {
            nightSwitch.setChecked(true);
        }

        nightSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {

                getSharePref().setNightMode(true);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                recreate();
            } else {
                getSharePref().setNightMode(false);
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                recreate();
            }
        });
    }

//    public void setTheme(){
//        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
//
//            setTheme(R.style.DarkTheme);
//        }
//        else setTheme(R.style.AppTheme);
//    }

//    public void restartApp() {
//        Intent i = new Intent(getApplicationContext(), NightmodeActivity.class);
//        startActivity(i);
//        finish();
//    }
}
