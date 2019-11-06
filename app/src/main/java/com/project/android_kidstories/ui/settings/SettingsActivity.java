package com.project.android_kidstories.ui.settings;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.project.android_kidstories.R;
import com.project.android_kidstories.ui.base.BaseActivity;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.framelayout_settings, new SettingsFragment())
                .commit();
    }
}
