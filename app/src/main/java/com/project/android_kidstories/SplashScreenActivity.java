package com.project.android_kidstories;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.ImageView;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation;
import android.os.Bundle;
import android.os.Handler;

import com.project.android_kidstories.Api.Responses.bookmark.BookmarkResponse;
import com.project.android_kidstories.DataStore.Repository;
import com.project.android_kidstories.Views.main.MainActivity;
import com.project.android_kidstories.sharePref.SharePref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreenActivity extends AppCompatActivity {

    SharePref sharePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        sharePref = SharePref.getINSTANCE(getApplicationContext());


        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade__in);
        ImageView fadeInImage = findViewById(R.id.logo);
        fadeInImage.startAnimation(fadeIn);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(sharePref.getIsUserLoggedIn()){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

                    startActivity(intent);
                }

                finish();
            }
            //the delay time is 3s
        }, 3000);

    }
}
