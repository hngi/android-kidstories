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

import java.io.File;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddStoriesContentActivity extends AppCompatActivity {
    public static final String TOKEN_KEY="token";
    private static final String TAG = "kidstories";
    public static String token = Prefs.getString(TOKEN_KEY, "");
    private static boolean isStoryAdded = false;
    private String title, body, ageInrange, author;
    private int category;
    private Repository repository;

    public final int PERMISSION_REQUEST_CODE = 100;


    EditText storyContent;
    Spinner categories;
    Button saveContent;
    public ProgressBar progressBar;

    Uri image_uri;
    String imageUri_str;
    Long storyCategoriesId;
    private RequestBody requestFile;
    private MultipartBody.Part image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stories_content);

        title = getIntent().getStringExtra("story_title");
        String image_path = getIntent().getStringExtra("image_path");
        assert image_path != null;
        imageUri_str = Uri.fromFile(new File(image_path)).toString();


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

                    AddStoryHelper.addOrUpdateStory(story, imageUri_str, true);
                }
//                Story story = new Story();
//                String filename = story.getTitle();
//                RequestBody requestFile = RequestBody.create(okhttp3.MultipartBody.FORM,"http://lorempixel.com/400/200/");
//                image = MultipartBody.Part.createFormData("photo", filename, requestFile);
//                addStoryToDatabase(title, body, category, image, ageInrange, author);
            }
        });
    }



//    public boolean addStory(Story story, String imageUri) {
//       // Uri uri = Uri.parse(imageUri);
//        //File imageFile = new File(Uri.decode(imageUri));
////        File imageFile = new File(imageUri);
////        String filename = story.getTitle();
////        RequestBody requestFile = RequestBody.create(okhttp3.MultipartBody.FORM,"http://lorempixel.com/400/200/");
////        image = MultipartBody.Part.createFormData("photo", filename, requestFile);
//
////        File file = new File(getRealPathFromURI(image_uri));
////
////        requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), file);
//
//        title = story.getTitle();
//        body = story.getBody();
//        category = story.getCategoryId();
//        ageInrange = story.getAge();
//        author = story.getAuthor();
//
////        RequestBody title = RequestBody.create(okhttp3.MultipartBody.FORM, story.getTitle());
////        RequestBody body = RequestBody.create(okhttp3.MultipartBody.FORM, story.getBody());
////        RequestBody category = RequestBody.create(okhttp3.MultipartBody.FORM, String.valueOf(story.getCategoryId()));
////        RequestBody ageInrange = RequestBody.create(okhttp3.MultipartBody.FORM, story.getAge());
////        RequestBody author = RequestBody.create(okhttp3.MultipartBody.FORM, story.getTitle());
////        RequestBody duration = RequestBody.create(okhttp3.MultipartBody.FORM, story.getStoryDuration());
//
//        return addStoryToDatabase(title, body, category, image, ageInrange, author);
//            //return addStoryToDatabase(title, body, category, ageInrange, author, duration, image);
//    }
//
//    private boolean addStoryToDatabase(String title, String body, int category, MultipartBody.Part image, String ageInrange, String author) {
//        progressBar.setVisibility(View.VISIBLE);
//        RetrofitClient.getInstance().create(Api.class).addStory(token, title, body, category, image, ageInrange, author)
//                .enqueue(new Callback<BaseResponse<Story>>() {
//                    @Override
//                    public void onResponse(Call<BaseResponse<Story>> call, Response<BaseResponse<Story>> response) {
//                        String message = response.body().getMessage();
//                        if (response.isSuccessful()) {
//                            Log.e(TAG, "onResponse: " + message);
//                            isStoryAdded=true;
//                            Toast.makeText(AddStoriesContentActivity.this, message, Toast.LENGTH_SHORT).show();
//                            progressBar.setVisibility(View.INVISIBLE);
//                        } else {
//                            isStoryAdded=false;
//                            Log.e(TAG, "onResponse: " + message);
//                            Toast.makeText(AddStoriesContentActivity.this, message, Toast.LENGTH_SHORT).show();
//                            progressBar.setVisibility(View.INVISIBLE);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<BaseResponse<Story>> call, Throwable t) {
//                        isStoryAdded=false;
//                        Log.e(TAG, "onFailure: "+t.getMessage());
//                        Toast.makeText(AddStoriesContentActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
//                        progressBar.setVisibility(View.INVISIBLE);
//                    }
//                });
//        return isStoryAdded;
//    }
//
//    public void saveContent(View view){
//        if(TextUtils.isEmpty(storyContent.getText().toString())){
//            storyContent.setError("Content cannot be empty");
//        }else if(categories.getSelectedItem().equals("Category")){
//            Toast.makeText(getApplicationContext(), "Please select a category", Toast.LENGTH_SHORT).show();
//        }else{
//            //checkPermission();
//            Story story = new Story();
//            story.setAge("2-5");
//            story.setTitle(title);
//            story.setBody(storyContent.getText().toString());
//            String author = Prefs.getString("Username", "");
//            story.setAuthor(author);
////            Log.i("apple", ""+addStory(story, image_uri));
//        }
//
//    }

}
