package com.project.android_kidstories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class SingleStoryActivity extends AppCompatActivity {

    private ImageView story_pic, like_btn;
    private TextView story_author , story_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_story);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Book Title");

        story_author = findViewById(R.id.author_name);
        story_content = findViewById(R.id.story_content);
        story_pic = findViewById(R.id.story_pic);
        like_btn = findViewById(R.id.like_button);
    }
}
