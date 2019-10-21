package com.project.android_kidstories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;
import com.project.android_kidstories.Api.Api;
import com.project.android_kidstories.Api.Responses.BaseResponse;
import com.project.android_kidstories.Api.RetrofitClient;
import com.project.android_kidstories.Model.Story;
import com.project.android_kidstories.R;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddStoriesContentActivity extends AppCompatActivity {
    public static final String TOKEN_KEY="token";
    private static final String TAG = "kidstories";
    public static String token = Prefs.getString(TOKEN_KEY, "");
    private static boolean isStoryAdded=false;

    public final int PERMISSION_REQUEST_CODE = 100;


    EditText storyContent;
    Spinner categories;
    Button saveContent;

    String story_title;
    String image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stories_content);

        story_title = getIntent().getStringExtra("story_title");
        //image_uri = getIntent().getStringExtra("image_uri");

        storyContent = findViewById(R.id.story_content_field);
        categories = findViewById(R.id.choose_category);
        saveContent = findViewById(R.id.save_content);



    }

    public static boolean addStory(Story story, String imageUri) {
       // Uri uri = Uri.parse(imageUri);
        //File imageFile = new File(Uri.decode(imageUri));
//        File imageFile = new File(imageUri);
        String filename = story.getTitle();
        RequestBody requestFile = RequestBody.create(okhttp3.MultipartBody.FORM,"http://lorempixel.com/400/200/");

        MultipartBody.Part image = MultipartBody.Part.createFormData("Image", filename, requestFile);
        RequestBody title = RequestBody.create(okhttp3.MultipartBody.FORM, story.getTitle());
        RequestBody body = RequestBody.create(okhttp3.MultipartBody.FORM, story.getBody());
        RequestBody category = RequestBody.create(okhttp3.MultipartBody.FORM, String.valueOf(story.getCategoryId()));
        RequestBody ageInrange = RequestBody.create(okhttp3.MultipartBody.FORM, story.getAge());
        RequestBody author = RequestBody.create(okhttp3.MultipartBody.FORM, story.getTitle());
//        RequestBody duration = RequestBody.create(okhttp3.MultipartBody.FORM, story.getStoryDuration());

        return addStoryToDatabase(title, body, category, ageInrange, author, image);
            //return addStoryToDatabase(title, body, category, ageInrange, author, duration, image);
    }

    private static boolean addStoryToDatabase(RequestBody title, RequestBody body, RequestBody category, RequestBody ageInrange, RequestBody author,MultipartBody.Part image) {
        RetrofitClient.getInstance().create(Api.class).addStory(token, title, body, category, ageInrange, author, image)
                .enqueue(new Callback<BaseResponse<Story>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<Story>> call, Response<BaseResponse<Story>> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, "onResponse: " + response.message());
                            isStoryAdded=true;
                        } else {
                            isStoryAdded=false;
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<Story>> call, Throwable t) {
                        isStoryAdded=false;
                        Log.d(TAG, "onFailure: "+t.getMessage());
                    }
                });
        return isStoryAdded;
    }

    public void saveContent(View view){
        if(TextUtils.isEmpty(storyContent.getText().toString())){
            storyContent.setError("Content cannot be empty");
        }else if(categories.getSelectedItem().equals("Category")){
            Toast.makeText(getApplicationContext(), "Please select a category", Toast.LENGTH_SHORT).show();
        }else{
            //checkPermission();
            Story story = new Story();
            story.setAge("2-5");
            story.setTitle(story_title);
            story.setBody(storyContent.getText().toString());
            String author = Prefs.getString("Username", "");
            story.setAuthor(author);
            Log.i("apple", ""+addStory(story, image_uri));
        }

    }


}
