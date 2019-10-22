package com.project.android_kidstories;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        /*Animation fade = AnimationUtils.loadAnimation(this, R.anim.fade__in);
        TextView transText = findViewById(R.id.about);
        transText.startAnimation(fade);*/
    }
}
