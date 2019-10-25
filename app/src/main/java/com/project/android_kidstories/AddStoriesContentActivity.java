package com.project.android_kidstories;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;
import com.project.android_kidstories.Api.Api;
import com.project.android_kidstories.Api.Responses.BaseResponse;
import com.project.android_kidstories.Api.RetrofitClient;
import com.project.android_kidstories.Model.Story;
import com.project.android_kidstories.Utils.FileUtil;
import com.project.android_kidstories.Views.main.MainActivity;
import com.project.android_kidstories.sharePref.SharePref;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class AddStoriesContentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final String TOKEN_KEY="token";
    private static final String TAG = "kidstories";
    private static boolean isStoryAdded = false;
    private static String title;
    private static String token;

    public final int PERMISSION_REQUEST_CODE = 100;

    EditText storyContent;
    Spinner categories;
    String storyBody;
    Button saveContent;
    public ProgressBar progressBar;
    public SharePref sharePref;

    Uri image_uri;
    String imageUri_str;
    String storyCategoriesId;
    private RequestBody storyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stories_content);

        title = getIntent().getStringExtra("story_title");
        String image_path = getIntent().getStringExtra("image_path");

        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.containsKey("image_uri")) {
            imageUri_str = extras.getString("image_uri");
        }

        image_uri = Uri.parse(imageUri_str);
        assert image_path != null;
//        image_uri = Uri.fromFile(new File(image_path));
        token = new SharePref(getApplicationContext()).getMyToken();
        token = "Bearer " + token;

        storyContent = findViewById(R.id.story_content_field);

        categories = findViewById(R.id.choose_category);
        categories.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.items_class,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categories.setAdapter(adapter);
        saveContent = findViewById(R.id.save_content);
        progressBar = findViewById(R.id.add_story_progress);

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
                    String author = new SharePref(getApplicationContext()).getUserFirstname();
                    story.setAuthor(author);

                    Log.d("TAG", story.getTitle());
                    Log.d("TAG", story.getAge());
                    Log.d("TAG", story.getAuthor());
                    Log.d("TAG", story.getBody());

                    addOrUpdateStory(story, image_uri);

                }
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Long storyCategory = parent.getItemIdAtPosition(position);
        storyCategoriesId = storyCategory.toString();
        Log.d("TAG", storyCategoriesId);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public boolean addOrUpdateStory(Story story, Uri imageUri) {
        String path = FileUtil.getPath(getApplicationContext(), imageUri);
        assert path != null;
        File file = new File(path);
//        File imageFile = new File(Uri.decode(imageUri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);

        MultipartBody.Part photo = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);

        assert story != null;

        RequestBody title = RequestBody.create(MediaType.parse("text/plain"), story.getTitle());
        RequestBody body = RequestBody.create(MediaType.parse("multipart/form-data"), story.getBody());
        RequestBody category_id = RequestBody.create(MediaType.parse("multipart/form-data"), storyCategoriesId);
        RequestBody ageInrange = RequestBody.create(MediaType.parse("multipart/form-data"), story.getAge());
        RequestBody author = RequestBody.create(MediaType.parse("multipart/form-data"), story.getAuthor());

        return addStory(title, body, category_id, photo, ageInrange, author);
    }

    private boolean addStory(RequestBody title, RequestBody  body, RequestBody category_id,  MultipartBody.Part photo, RequestBody ageInrange, RequestBody author) {
        progressBar.setVisibility(View.VISIBLE);
        RetrofitClient.getInstance().create(Api.class).addStory(token, title, body, category_id, photo, ageInrange, author)
                .enqueue(new Callback<BaseResponse<Story>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<Story>> call, Response<BaseResponse<Story>> response) {
                        assert response.body() != null;
                        String messageResp = response.message();
                        if (response.code() == 200){
                            isStoryAdded = true;
                            Toast.makeText(getApplicationContext(), "Story Created", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(AddStoriesContentActivity.this, MainActivity.class));
                            progressBar.setVisibility(View.INVISIBLE);
                        } else {
                            isStoryAdded = false;
                            Toast.makeText(getApplicationContext(), messageResp, Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<Story>> call, Throwable t) {
                        isStoryAdded = false;
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });

        return isStoryAdded;

    }

}
