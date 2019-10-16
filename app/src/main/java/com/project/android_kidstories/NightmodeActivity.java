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

public class NightmodeActivity extends AppCompatActivity {
    private Switch nightswitch;
    //    This is supposed to change the view of the app from Light to Dark mode.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // getActionBar().setDisplayHomeAsUpEnabled(true);

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        }
        else setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nightmode);

        BaseActivity base = new BaseActivity();
        SharePref shared = base.getSharePref();

        nightswitch = findViewById(R.id.night_switch);


        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            nightswitch.setChecked(true);
        }

        nightswitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                shared.setNightMode(true);
                Log.d("XXXX",String.valueOf(shared.getNightMode()));
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                restartApp();
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                restartApp();
            }
        });
    }

    public void restartApp() {
        Intent i = new Intent(getApplicationContext(), NightmodeActivity.class);
        startActivity(i);
        finish();
    }
}
