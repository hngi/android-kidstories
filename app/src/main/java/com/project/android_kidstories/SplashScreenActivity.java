package com.project.android_kidstories;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.project.android_kidstories.Views.main.MainActivity;
import com.project.android_kidstories.sharePref.SharePref;

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

                if (sharePref.getIsUserLoggedIn()) {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                } else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                    startActivity(intent);
                }

                finish();
            }
            //the delay time is 3s
        }, 3000);

    }
}
