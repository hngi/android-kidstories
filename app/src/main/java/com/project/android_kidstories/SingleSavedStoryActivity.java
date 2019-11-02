package com.project.android_kidstories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.project.android_kidstories.Model.Story;
import com.project.android_kidstories.adapters.SavedStoriesAdapter;
import com.project.android_kidstories.database.StoryLab;


public class SingleSavedStoryActivity extends AppCompatActivity {
    Story story ;
    StoryLab storyLab;
    TextView authorTv, storyContentTv;
    private Toolbar toolbar;

    private ImageButton ZoomIn, ZoomOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_saved_story);
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        String storyTitle = getIntent().getStringExtra(SavedStoriesAdapter.KEY_STORY_TITLE);



        storyLab= StoryLab.get(this);
        story = storyLab.getStory(storyTitle);

        authorTv = findViewById(R.id.saved_story_author);
        storyContentTv = findViewById(R.id.saved_story_content);

        ZoomIn = findViewById(R.id.Zoom_In);
        ZoomOut = findViewById(R.id.Zoom_Out);
        ZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storyContentTv.getTextSize();
                storyContentTv.setTextSize(24);
                storyContentTv.setMovementMethod(new ScrollingMovementMethod());
            }
        });
        ZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storyContentTv.getTextSize();
                storyContentTv.setTextSize(14);
                storyContentTv.setMovementMethod(new ScrollingMovementMethod());
            }
        });


        if (story==null){

            Toast.makeText(this, "story file not found", Toast.LENGTH_SHORT).show();
            finish();


        }
        else{
            if(storyTitle!=null){
                getSupportActionBar().setTitle(storyTitle);
                showStory(storyLab.getStory(storyTitle));
            }
        }
    }

    public void showStory(Story story){
        authorTv.setText(story.getAuthor());
        storyContentTv.setText(story.getBody());

    }


}
