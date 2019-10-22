package com.project.android_kidstories;

import android.os.Bundle;
import android.widget.Switch;
import com.project.android_kidstories.base.BaseActivity;

public class NightmodeActivity extends BaseActivity {

    //    This is supposed to change the view of the app from Light to Dark mode.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // getActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nightmode);

        Switch nightSwitch = findViewById(R.id.night_switch);

        if (getSharePref().getNightMode()) {
            nightSwitch.setChecked(true);
        }

        nightSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                getSharePref().setNightMode(true);
            } else {
                getSharePref().setNightMode(false);
            }
        });


    }

}
