package com.project.android_kidstories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.project.android_kidstories.Model.Story;
import com.project.android_kidstories.Views.main.MainActivity;

import java.util.HashSet;
import java.util.Set;
/**
 * @author .: Utibe Etim
 * @email ..: etim.utibe@gmail.com
 * @created : 17/10/19
 */
public class SingleStoryActivity extends AppCompatActivity {
    public static final String STORY_POSITION = "story_position";
    public static final String USER_KEY_INTENT_EXTRA ="com.project.android_kidstories_USER_KEY";
    public static final String PREFERENCE_NAME = "com.project.android_kidstories";
    public static final String PREFERENCE_KEY_NAME = "favourite";
    Set<String> mFavourite;
    private ImageView story_pic;
    private ImageView like_btn;
    private TextView story_author;
    private TextView story_content;
    int story_id = 2;

    ProgressDialog progressDoalog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_story);

        initViews();


    }

    private void initViews() {
        progressDoalog = new ProgressDialog(SingleStoryActivity.this);
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        story_author = findViewById(R.id.author_name_tv);
        story_content = findViewById(R.id.story_content_tv);
        story_pic = findViewById(R.id.sigle_story_thumbnail);
        like_btn = findViewById(R.id.fav_btn);
        story_id = getIntent().getIntExtra("story_id", 0);
        Intent intent = getIntent();

        mFavourite = getFavourite();

        if (intent !=null){

            Story story = getIntent().getParcelableExtra("story");
            //story.ge
            Glide.with(this).load(story.getImageUrl()).into(story_pic);

            story_author.setText(story.getAuthor());
            story_content.setText(story.getBody());
            
        }

        //saveFavourite();
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

//    public void fav(View v){
//
//        if (ConfirmFavourite (Position)){
//            mFavourite.remove(Integer.toString(Position));
//            Toast.makeText(SingleStoryActivity.this,"Removed from favorite list",Toast.LENGTH_SHORT).show();
//        }else {
//            mFavourite.add(Integer.toString(Position));
//            Toast.makeText(SingleStoryActivity.this,"Added to your favorite list", Toast.LENGTH_SHORT).show();
//        }
//        saveFavourite();
//    }
    private boolean ConfirmFavourite(int isPosition) {
        Set<String> FavCF = getFavourite();
        if (FavCF.contains(Integer.toString(isPosition))){
            return true;
        }else {
            return false;
        }
    }

//    private void saveFavourite() {
//        SharedPreferences prefs = getSharedPreferences(PREFERENCE_NAME, 0);
//        SharedPreferences.Editor editP = prefs.edit();
//        editP.putStringSet(PREFERENCE_KEY_NAME, mFavourite).apply();
//        if (ConfirmFavourite(Position)){
//            like_btn.setImageResource(R.drawable.ic_main_cards_green);
//        }else {
//            like_btn.setImageResource(R.drawable.ic_main_cards_green);
//        }
//    }

    private Set<String> getFavourite() {
        SharedPreferences prefs = getSharedPreferences(PREFERENCE_NAME, 0);
        return prefs.getStringSet(PREFERENCE_KEY_NAME, new HashSet<String>());
    }
}
