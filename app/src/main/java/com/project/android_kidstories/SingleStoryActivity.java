package com.project.android_kidstories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class SingleStoryActivity extends AppCompatActivity {

    private ImageView story_pic, like_btn;
    private TextView story_author , story_content;
    int story_id = 2;

    ProgressDialog progressDoalog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_story);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDoalog = new ProgressDialog(SingleStoryActivity.this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        story_author = findViewById(R.id.author_name);
        story_content = findViewById(R.id.story_content);
        story_pic = findViewById(R.id.story_pic);
        like_btn = findViewById(R.id.like_button);
        int story_id = getIntent().getIntExtra("story_id", 0);

       /* ApiInterface service = Client.getInstance().create(ApiInterface.class);
        Call<StoryResponse> story = service.getStory(story_id);

        story.enqueue(new Callback<StoryResponse>() {
            @Override
            public void onResponse(Call<StoryResponse> call, Response<StoryResponse> response) {
                progressDoalog.dismiss();
                Log.i("apple", response.message());
                Story currentStory = response.body().getData();
                story_author.setText(currentStory.getAuthor());
                story_content.setText(currentStory.getBody());

                getSupportActionBar().setTitle(currentStory.getTitle());
            }

            @Override
            public void onFailure(Call<StoryResponse> call, Throwable t) {
                progressDoalog.dismiss();
                Toast.makeText(SingleStoryActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });*/
    }
}
