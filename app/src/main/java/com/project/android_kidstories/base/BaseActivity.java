package com.project.android_kidstories.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.project.android_kidstories.LoginActivity;
import com.project.android_kidstories.Model.User;
import com.project.android_kidstories.R;
import com.project.android_kidstories.RegisterActivity;
import com.project.android_kidstories.Views.main.MainActivity;
import com.project.android_kidstories.sharePref.SharePref;

public class BaseActivity extends AppCompatActivity {
    private SharePref sharePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setThemes();
        super.onCreate(savedInstanceState);
        sharePref=SharePref.getINSTANCE(this);

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
