package com.project.android_kidstories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.project.android_kidstories.Api.Api;
import com.project.android_kidstories.Api.Responses.story.StoryBaseResponse;
import com.project.android_kidstories.DataStore.Repository;
import com.project.android_kidstories.Model.Story;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleStoryActivity extends AppCompatActivity {

    private ImageView story_pic, like_btn;
    private TextView story_author , story_content;
    int story_id = 0;

    ProgressDialog progressDialog;
    private Repository repository;
    private Api storyApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_story);

        repository = Repository.getInstance(this.getApplication());
        storyApi = repository.getStoryApi();
        story_id = getIntent().getIntExtra("story_id", 0);


        progressDialog = new ProgressDialog(SingleStoryActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        story_author = findViewById(R.id.author);
        story_content = findViewById(R.id.story_content);
        story_pic = findViewById(R.id.story_pic);
        like_btn = findViewById(R.id.like_button);
        //todo : check authorization for premium stories
        getStoryWithId(story_id);
    }

    public void getStoryWithId(int id){
        storyApi.getStory(id).enqueue(new Callback<StoryBaseResponse>() {
            @Override
            public void onResponse(Call<StoryBaseResponse> call, Response<StoryBaseResponse> response) {
                try{
                    progressDialog.dismiss();

                    Story currentStory = response.body().getData();
                    story_author.setText(currentStory.getAuthor());
                    story_content.setText(currentStory.getBody());
                } catch(Exception e){
                    Toast.makeText(SingleStoryActivity.this, "Oops Something went wrong ... story specific issue",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<StoryBaseResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(SingleStoryActivity.this, "Oops Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
