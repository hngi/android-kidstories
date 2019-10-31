package com.project.android_kidstories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.project.android_kidstories.Model.Story;
import com.project.android_kidstories.adapters.SavedStoriesAdapter;
import com.project.android_kidstories.database.StoryLab;

import java.util.ArrayList;
import java.util.List;

public class SavedStoriesActivity extends AppCompatActivity {

    StoryLab storyLab;

    RecyclerView recyclerView;
    SavedStoriesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_stories);
        storyLab = StoryLab.get(this);

        Story story = new Story();
        story.setTitle("Monkey Story");
        story.setId(1);
        story.setAuthor("Joshua Erondu");
        story.setBody("story body");
        story.setAge("4-9");

        recyclerView = findViewById(R.id.saved_stories_recycler);
        List<Story> stories = new ArrayList<Story>();
        stories.add(story);

        adapter = new SavedStoriesAdapter(this,stories);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


    }


}
