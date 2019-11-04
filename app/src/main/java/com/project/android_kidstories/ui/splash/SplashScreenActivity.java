package com.project.android_kidstories.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.project.android_kidstories.R;
import com.project.android_kidstories.sharePref.SharePref;
import com.project.android_kidstories.ui.MainActivity;
import com.project.android_kidstories.ui.login.LoginActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final SharePref sharePref = SharePref.getINSTANCE(getApplicationContext());

        View logoImg = findViewById(R.id.img_logo_splash);
        logoImg.setAlpha(0f);
        // Animate
        logoImg.animate()
                .alpha(1f)
                .setDuration(600)
                .start();

        //the delay time is 2s
        new Handler().postDelayed(() -> {
            if (sharePref.getIsUserLoggedIn()) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            } else {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }

            finish();
        }, 2500);
    }
}
