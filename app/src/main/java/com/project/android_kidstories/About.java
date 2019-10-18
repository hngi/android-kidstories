package com.project.android_kidstories;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.project.android_kidstories.R;

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
