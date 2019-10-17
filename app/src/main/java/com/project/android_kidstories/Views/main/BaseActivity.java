package com.project.android_kidstories.Views.main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.project.android_kidstories.R;
import com.project.android_kidstories.sharePref.SharePref;

public class BaseActivity extends AppCompatActivity {
    private SharePref sharePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setThemes();
        super.onCreate(savedInstanceState);
        sharePref= SharePref.getINSTANCE(this);

    }

    public SharePref getSharePref() {
        return sharePref;
    }

    public void setThemes(){
        if (SharePref.getINSTANCE(this).getNightMode()) {

            setTheme(R.style.DarkTheme);
        }
        else setTheme(R.style.AppTheme);
    }

}
