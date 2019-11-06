package com.project.android_kidstories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.project.android_kidstories.Model.Story;
import com.project.android_kidstories.adapters.SavedStoriesAdapter;
import com.project.android_kidstories.database.StoryLab;
import com.project.android_kidstories.sharePref.SharePref;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;


public class SingleSavedStoryActivity extends AppCompatActivity {
    Story story ;
    StoryLab storyLab;
    ImageView  storyImage;
    TextView authorTv, storyContentTv;
    ImageButton btn_speak;
    ImageButton btn_stop;
    TextView speak_text;
    TextToSpeech textToSpeech;
    String googleTtsPackage = "com.google.android.tts", picoPackage = "com.svox.pico";

    private Toolbar toolbar;

    private ImageButton ZoomIn, ZoomOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_saved_story);
        toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String storyTitle = getIntent().getStringExtra(SavedStoriesAdapter.KEY_STORY_TITLE);



        storyLab= StoryLab.get(this);
        story = storyLab.getStory(storyTitle);

        authorTv = findViewById(R.id.saved_story_author);
        storyContentTv = findViewById(R.id.saved_story_content);
        storyImage = findViewById(R.id.story_pic);

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
        Bitmap bitmap = loadBitmap(this,story.getTitle()+".png");
        storyImage.setImageBitmap(bitmap);


        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS)
                {
                    int result = textToSpeech.setLanguage(Locale.ENGLISH);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED)
                    {
                        Toast.makeText(SingleSavedStoryActivity.this, "This Language is not Supported", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        btn_speak.setEnabled(true);
                        //textToSpeech.setPitch(0.6f);
                        textToSpeech.setEngineByPackageName(googleTtsPackage);
                        textToSpeech.setSpeechRate(0.85f);
                    }
                }

            }
        });

        speak_text = findViewById(R.id.saved_story_content);
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



    }

    public static Bitmap loadBitmap(Context context, String picName){
        Bitmap b = null;
        FileInputStream fis;
        try {
            fis = context.openFileInput(picName);
            b = BitmapFactory.decodeStream(fis);
        }
        catch (FileNotFoundException e) {
            Log.d("tag", "file not found");
            e.printStackTrace();
        }
        catch (IOException e) {
            Log.d("tag", "io exception");
            e.printStackTrace();
        } finally {

        }
        return b;
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
        super.onDestroy();

    }  @Override
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





}
