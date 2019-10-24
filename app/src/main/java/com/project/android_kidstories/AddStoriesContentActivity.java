package com.project.android_kidstories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.pixplicity.easyprefs.library.Prefs;
import com.project.android_kidstories.Api.Api;
import com.project.android_kidstories.Api.HelperClasses.AddStoryHelper;
import com.project.android_kidstories.Api.Responses.BaseResponse;
import com.project.android_kidstories.Api.RetrofitClient;
import com.project.android_kidstories.DataStore.Repository;
import com.project.android_kidstories.Model.Story;
import com.project.android_kidstories.sharePref.SharePref;

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
    private static boolean isStoryAdded = false;
    private static String title, token;

    public final int PERMISSION_REQUEST_CODE = 100;

    EditText storyContent;
    Spinner categories;
    Button saveContent;
    public ProgressBar progressBar;
    public SharePref sharePref;

    Uri image_uri;
    String imageUri_str;
    Long storyCategoriesId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stories_content);

        title = getIntent().getStringExtra("story_title");
        String image_path = getIntent().getStringExtra("image_path");
        assert image_path != null;
        imageUri_str = Uri.fromFile(new File(image_path)).toString();
        token = sharePref.getMyToken();

        storyContent = findViewById(R.id.story_content_field);
        categories = findViewById(R.id.choose_category);
        storyCategoriesId = categories.getSelectedItemId();
        saveContent = findViewById(R.id.save_content);

        saveContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(storyContent.getText().toString())){
                    storyContent.setError("Content cannot be empty");
                }else if(categories.getSelectedItem().equals("Category")){
                    Toast.makeText(getApplicationContext(), "Please select a category", Toast.LENGTH_SHORT).show();
                }else {
                    //checkPermission();
                    Story story = new Story();
                    story.setAge("2-5");
                    story.setTitle(title);
                    story.setBody(storyContent.getText().toString());
                    String author = Prefs.getString("Username", "");
                    story.setAuthor(author);

                    addOrUpdateStory(story, imageUri_str);
                }
            }
        });
    }

    public static boolean addOrUpdateStory(Story story, String imageUri) {
        File imageFile = new File(Uri.decode(imageUri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);

        MultipartBody.Part photo = MultipartBody.Part.createFormData("Image", imageFile.getName(), requestFile);
        RequestBody title = RequestBody.create(okhttp3.MultipartBody.FORM, story.getTitle());
        RequestBody body = RequestBody.create(okhttp3.MultipartBody.FORM, story.getBody());
        RequestBody category = RequestBody.create(okhttp3.MultipartBody.FORM, String.valueOf(story.getCategoryId()));
        RequestBody ageInrange = RequestBody.create(okhttp3.MultipartBody.FORM, story.getAge());
        RequestBody author = RequestBody.create(okhttp3.MultipartBody.FORM, story.getTitle());

        return addStory(title, body, category, photo, ageInrange, author);
    }

    private static boolean addStory(RequestBody title, RequestBody  body, RequestBody category,  MultipartBody.Part photo, RequestBody ageInrange, RequestBody author) {

        RetrofitClient.getInstance().create(Api.class).addStory(token, title, body, category, photo, ageInrange, author)
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

}
