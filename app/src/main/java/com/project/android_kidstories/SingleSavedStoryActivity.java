package com.project.android_kidstories;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.project.android_kidstories.Model.Story;
import com.project.android_kidstories.adapters.SavedStoriesAdapter;
import com.project.android_kidstories.database.StoryLab;


public class SingleSavedStoryActivity extends AppCompatActivity {
    Story story ;
    StoryLab storyLab;
    TextView authorTv, storyContentTv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_saved_story);
        int storyId = getIntent().getIntExtra(SavedStoriesAdapter.KEY_STORY_ID,-1);
        String storyTitle = getIntent().getStringExtra(SavedStoriesAdapter.KEY_STORY_TITLE);

        storyLab= StoryLab.get(this);

        authorTv = findViewById(R.id.saved_story_content);
        storyContentTv = findViewById(R.id.saved_story_content);

        story = storyLab.getStory(storyTitle);

        if (story==null){

            Toast.makeText(this, "story file not found", Toast.LENGTH_SHORT).show();
            finish();


        }
        else{
            if(!storyTitle.equals(null)){

                showStory(storyLab.getStory(storyTitle));

            }
        }
    }

    public void showStory(Story story){
        storyContentTv.setText(story.getBody());
    }
}
