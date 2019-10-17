package com.project.android_kidstories;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.project.android_kidstories.R;

public class StoryListingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_listing);

        CollapsingToolbarLayout toolbar = findViewById(R.id.Collapse_toolbar);
        Toolbar toolbar1 = findViewById(R.id.toolbar);
        toolbar.setTitle("Category Name");
        setSupportActionBar(toolbar1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
