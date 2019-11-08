/*
package com.project.android_kidstories;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.project.android_kidstories.adapters.SavedStoriesAdapter;
import com.project.android_kidstories.data.source.local.relational.database.StoryLab;

public class SavedStoriesActivity extends AppCompatActivity {

    StoryLab storyLab;

    RecyclerView recyclerView;
    SavedStoriesAdapter adapter;
    private Toolbar toolbar;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_downloads);
        storyLab = StoryLab.get(this);
        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Saved Stories");
        recyclerView = findViewById(R.id.saved_stories_recycler);

        adapter = new SavedStoriesAdapter(this, storyLab.getStories());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                finish();
                break;
        }
        return  true;
    }
}
*/
