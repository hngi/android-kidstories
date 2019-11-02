package com.project.android_kidstories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.project.android_kidstories.Model.Story;
import com.project.android_kidstories.Views.main.MainActivity;
import com.project.android_kidstories.adapters.SavedStoriesAdapter;
import com.project.android_kidstories.database.StoryLab;

import java.util.ArrayList;
import java.util.List;

public class SavedStoriesActivity extends AppCompatActivity {

    StoryLab storyLab;

    RecyclerView recyclerView;
    SavedStoriesAdapter adapter;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_stories);
        storyLab = StoryLab.get(this);

        recyclerView = findViewById(R.id.saved_stories_recycler);

        adapter = new SavedStoriesAdapter(this, storyLab.getStories());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
