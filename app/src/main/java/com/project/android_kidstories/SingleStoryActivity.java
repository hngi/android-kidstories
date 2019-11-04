package com.project.android_kidstories;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import com.bumptech.glide.Glide;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.project.android_kidstories.data.Repository;
import com.project.android_kidstories.data.model.Comment;
import com.project.android_kidstories.data.model.ReadStory;
import com.project.android_kidstories.data.model.Story;
import com.project.android_kidstories.data.source.local.preferences.SharePref;
import com.project.android_kidstories.data.source.local.relational.database.StoryLab;
import com.project.android_kidstories.data.source.remote.api.Api;
import com.project.android_kidstories.data.source.remote.response_models.story.StoryBaseResponse;
import com.project.android_kidstories.ui.base.BaseActivity;
import com.project.android_kidstories.ui.reading_status.ReadingStatusActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SingleStoryActivity extends BaseActivity {

    private MediaPlayer backgroundMusicPlayer;
    private ImageView story_pic, like_btn;
    int story_id = 0;
    private TextView story_author, story_content, error_msg, saveStory;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private Repository repository;
    private Api storyApi;

    Story testStory;
    StoryLab storyLab;
    ImageButton btn_speak;
    ImageButton btn_stop;
    TextView speak_text;
    TextToSpeech textToSpeech;
    SharePref sharePref;
    Button comment_btn;
    String googleTtsPackage = "com.google.android.tts", picoPackage = "com.svox.pico";

    LikeButton likeButton;

    ImageButton playButton;
    ImageButton stopButton;

    private ImageButton ZoomIn, ZoomOut;
    private static List<Comment> comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_story);
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        storyLab = StoryLab.get(this);
        ZoomIn = findViewById(R.id.Zoom_In);
        ZoomOut = findViewById(R.id.Zoom_Out);

        repository = Repository.getInstance(this.getApplication());
        storyApi = repository.getStoryApi();
        story_id = getIntent().getIntExtra("story_id", 0);

        sharePref = getSharePref();

        Button markAsReadBtn = findViewById(R.id.btn_markasread);
        // Check if story has been read already
        repository.getStoryForId(String.valueOf(story_id)).observe(this, readStory -> {
            if (readStory == null) {
                markAsReadBtn.setVisibility(View.VISIBLE);
            }
        });

        // For controlling Zooming In
        ZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                story_content.getTextSize();
                story_content.setTextSize(24);
                story_content.setMovementMethod(new ScrollingMovementMethod());
            }
        });


        // For controlling Zooming Out
        ZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                story_content.getTextSize();
                story_content.setTextSize(14);
                story_content.setMovementMethod(new ScrollingMovementMethod());
            }
        });

        markAsReadBtn.setOnClickListener(view -> {
            int storiesRead = sharePref.getInt(ReadingStatusActivity.STORIES_READ_KEY);
            storiesRead += 1;
            sharePref.setInt(ReadingStatusActivity.STORIES_READ_KEY, storiesRead);

            ReadingStatusActivity.displayUserReadingStatus(this, storiesRead);

            repository.insertReadStoryId(new ReadStory(String.valueOf(story_id)));
            markAsReadBtn.setVisibility(View.GONE);
        });

        progressBar = findViewById(R.id.story_content_bar);
        progressBar.setVisibility(View.VISIBLE);

        story_author = findViewById(R.id.author);
        story_content = findViewById(R.id.story_content);
        story_pic = findViewById(R.id.story_pic);
        like_btn = findViewById(R.id.like_button);
        error_msg = findViewById(R.id.error_msg);
        saveStory = findViewById(R.id.save_story);
        //todo : check authorization for premium stories
        getStoryWithId(story_id);

        //Favorite button functionality

        likeButton = findViewById(R.id.heart_button);
        likeButton.setLiked(false);


        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

                likeButton.setEnabled(true);

            }

            @Override
            public void unLiked(LikeButton likeButton) {

                likeButton.setEnabled(true);
            }
        });

        comment_btn =findViewById(R.id.comment_btn);
        comment_btn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                sendCommentList();
            }
        });

        saveStory.setVisibility(View.INVISIBLE);
        saveStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (testStory!=null){
                    if(storyLab.getStory(testStory.getTitle())==null){
                        storyLab.addStory(testStory);

                        BitmapDrawable bitmapDrawable = (BitmapDrawable) story_pic.getDrawable();
                        Bitmap bitmap = bitmapDrawable .getBitmap();
                        saveImageFile(SingleStoryActivity.this
                                ,bitmap
                                , testStory.getTitle()+".png");
                        Toast.makeText(SingleStoryActivity.this, "Story saved", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        replaceSavedStoryDialog(testStory);
                    }
                }
            }
        });
    }

    public void getStoryWithId(int id) {
        storyApi.getStory(id).enqueue(new Callback<StoryBaseResponse>() {
            @Override
            public void onResponse(Call<StoryBaseResponse> call, Response<StoryBaseResponse> response) {
                try {
                    Story currentStory = response.body().getData();
                    testStory = currentStory;
                    getSupportActionBar().setTitle(currentStory.getTitle());
                    story_author.setText(currentStory.getAuthor());
                    story_content.setText(currentStory.getBody());
                    Glide.with(getApplicationContext()).load(currentStory.getImageUrl()).placeholder(R.drawable.story_bg_ic).into(story_pic);
                    story_author.setVisibility(View.VISIBLE);
                    story_content.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    saveStory.setVisibility(View.VISIBLE);
                    comments = currentStory.getComments().getComments();

                } catch (Exception e) {
                    Toast.makeText(SingleStoryActivity.this, "Oops Something went wrong ... story specific issue", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    error_msg.setVisibility(View.VISIBLE);
                    story_author.setVisibility(View.INVISIBLE);
                    story_content.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<StoryBaseResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                story_author.setVisibility(View.INVISIBLE);
                story_content.setVisibility(View.INVISIBLE);
                error_msg.setVisibility(View.VISIBLE);
                Toast.makeText(SingleStoryActivity.this, "Oops Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });


        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS)
                {
                    int result = textToSpeech.setLanguage(Locale.ENGLISH);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED)
                    {
                        Toast.makeText(SingleStoryActivity.this, "This Language is not Supported", Toast.LENGTH_SHORT).show();
                    }
                    else {
                    btn_speak.setEnabled(true);
                    //textToSpeech.setPitch(0.6f);
                    textToSpeech.setEngineByPackageName(googleTtsPackage);
                    textToSpeech.setSpeechRate(0.85f);
                    speak();}
                }

            }
        });

        speak_text = findViewById(R.id.story_content);
        btn_speak = findViewById(R.id.play_story);
        btn_stop = findViewById(R.id.stop_story);

        btn_speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speak();
              if (textToSpeech.isSpeaking()){
                  btn_speak.setVisibility(View.INVISIBLE);
              btn_stop.setVisibility(View.VISIBLE);
              btn_stop.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      textToSpeech.stop();
                      btn_speak.setVisibility(View.VISIBLE);
                      btn_stop.setVisibility(View.INVISIBLE);
                  }
              });}




            }
        });


        //background Music

        backgroundMusicPlayer = MediaPlayer.create(this, R.raw.kidsong2);
        playButton = findViewById(R.id.playSong);
        stopButton = findViewById(R.id.stopSong);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
                if (backgroundMusicPlayer.isPlaying()){
                    playButton.setVisibility(View.INVISIBLE);
                    stopButton.setVisibility(View.VISIBLE);
                    stopButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            backgroundMusicPlayer.pause();
                            playButton.setVisibility(View.VISIBLE);
                            stopButton.setVisibility(View.INVISIBLE);
                        }
                    });}
            }
        });

    }


    private void sendCommentList(){
        Intent intent = new Intent(SingleStoryActivity.this, CommentActivity.class);
        intent.putExtra("storyId", story_id);
        startActivity(intent);
    }

    public static List<Comment> returnComments(){
        return comments;
    }

    private void play() {
        backgroundMusicPlayer.start();
        backgroundMusicPlayer.setLooping(true);
    }

    private void speak() {

        String text = speak_text.getText().toString();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH,null,null);
        }
        else {textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH,null);}
    }

    @Override
    protected void onDestroy() {

        if (textToSpeech != null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }

        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
            backgroundMusicPlayer.release();
        }

        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        textToSpeech.stop();
        textToSpeech.shutdown();
    }

    @Override
    protected void onResume() {
        super.onResume();
        btn_speak.setVisibility(View.VISIBLE);
        btn_speak.setEnabled(true);
        btn_stop.setVisibility(View.INVISIBLE);

    }

    public void replaceSavedStoryDialog(Story story){
         new AlertDialog.Builder(SingleStoryActivity.this)
                 .setMessage("A story with this name already exists.\nDo you want to Replace it?" )
                 .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialogInterface, int i) {
                         storyLab.deleteStory(story);
                         storyLab.addStory(story);
                     }
                 })
                 .setNegativeButton("no" , null).show();
    }
    public static void saveImageFile(Context context, Bitmap b, String picName){
        FileOutputStream fos ;
        try {
            fos = context.openFileOutput(picName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
        }
        catch (FileNotFoundException e) {

            Log.d("TAG", "file not found");
            e.printStackTrace();
        }
        catch (IOException e) {
            Log.d("TAG", "io exception");
            e.printStackTrace();
        } finally {
                   }
    }
}
