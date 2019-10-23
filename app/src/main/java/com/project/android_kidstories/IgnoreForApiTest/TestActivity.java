package com.project.android_kidstories.IgnoreForApiTest;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.pixplicity.easyprefs.library.Prefs;
import com.project.android_kidstories.Api.Api;
import com.project.android_kidstories.Api.Responses.BaseResponse;
import com.project.android_kidstories.Api.Responses.Category.CategoriesAllResponse;
import com.project.android_kidstories.Api.Responses.Category.CategoryStoriesResponse;
import com.project.android_kidstories.Api.Responses.bookmark.BookmarkResponse;
import com.project.android_kidstories.Api.Responses.bookmark.UserBookmarkResponse;
import com.project.android_kidstories.Api.Responses.comment.CommentResponse;
import com.project.android_kidstories.Api.Responses.loginRegister.DataResponse;
import com.project.android_kidstories.Api.Responses.loginRegister.LoginResponse;
import com.project.android_kidstories.Api.Responses.story.StoryAllResponse;
import com.project.android_kidstories.DataStore.Repository;
import com.project.android_kidstories.Model.Category;
import com.project.android_kidstories.Model.Story;
import com.project.android_kidstories.Model.User;
import com.project.android_kidstories.R;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;


/**
 * @author .: Ehma Ugbogo
 * @email ..: ehmaugbogo@gmail.com
 * @created : 16/10/19
 */


// Please ignore this activity, first its for test purpose. Secondly, its to help beginners make Retrofit
// api Implementation. this class will be deleted later

//To use in yours: replace Textview with logd and log errors so you see whats happening via logs


public class TestActivity extends AppCompatActivity {

    private Repository repository;
    private TextView textView;
    private Api storyApi;
    private ProgressBar progressBar;
    private String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        repository = Repository.getInstance(this.getApplication());
        storyApi = repository.getStoryApi();

        textView = findViewById(R.id.test_textView);
        progressBar = findViewById(R.id.test_progressbar);


    }


    public void RegisterUser(View view) {
        User user = new User("Ehma", "Ugbogo", "ehmaugbogo@gmail.com");
        user.setPassword("12345678");
        user.setPhoneNumber("08107535626");
        showProgressbar();
        repository.getStoryApi().registerUser(user).enqueue(new Callback<BaseResponse<DataResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<DataResponse>> call, Response<BaseResponse<DataResponse>> response) {
                if (response.isSuccessful()) {
                    String code = response.body().getCode();
                    String message = response.body().getMessage();
                    textView.setText("Response Code " + code + ": " + message + "\n");
                    textView.append("FirstName: " + response.body().getData().getFirstName() + "\n");
                    textView.append("Token: " + response.body().getData().getToken());
                    Prefs.putString("Token", response.body().getData().getToken());
                } else {
                    textView.setText("Success Error " + response.message());
                    textView.append("Code " + response.code());

                }
                hideProgressbar();
            }

            @Override
            public void onFailure(Call<BaseResponse<DataResponse>> call, Throwable t) {
                textView.setText("Response Error " + t.getMessage());
                hideProgressbar();
            }
        });
    }

    public void LoginUser(View view) {
        showProgressbar();
        repository.getStoryApi().loginUser("email@email.com", "apass123").enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    textView.setText("Response Status " + status + ": " + message + "\n");
                    textView.append("FirstName: " + response.body().getUser().getFirstName() + "\n");
                    textView.append("Token: " + response.body().getUser().getToken());
                    Prefs.putString("Token", response.body().getUser().getToken());
                } else {
                    textView.setText("Success Error " + response.message());
                    textView.append("Code " + response.code());

                }
                hideProgressbar();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                textView.setText("Response Error " + t.getMessage());
                hideProgressbar();
            }
        });
    }

    //
    public void getAllStories(View view) {
        showProgressbar();
        storyApi.getAllStories().enqueue(new Callback<StoryAllResponse>() {
            @Override
            public void onResponse(Call<StoryAllResponse> call, Response<StoryAllResponse> response) {
                if (response.isSuccessful()) {
                    String status = response.body().getStatus();
                    String message = response.body().getMessage();
                    textView.setText("Response Status " + status + ": " + message + "\n");

                    for (Story s : response.body().getData()) {
                        textView.append("StoryTitle: " + s.getTitle() + "\n");
                    }

                } else {
                    textView.setText("Success Error " + response.message());
                    textView.append("Code " + response.code());

                }
                hideProgressbar();
            }

            @Override
            public void onFailure(Call<StoryAllResponse> call, Throwable t) {
                textView.setText("Response Error " + t.getMessage());
                hideProgressbar();
            }
        });
    }


    public void getAllCategory(View view) {
        showProgressbar();
        storyApi.getAllCategories().enqueue(new Callback<CategoriesAllResponse>() {
            @Override
            public void onResponse(Call<CategoriesAllResponse> call, Response<CategoriesAllResponse> response) {
                if (response.isSuccessful()) {
                    String status = String.valueOf(response.body().getStatus());
                    String message = response.body().getMessage();
                    textView.setText("Response Status " + status + ": " + message + "\n");

                    List<Category> data = response.body().getData();
                    for (Category s : data) {
                        textView.append("CategoryTitle: " + s.getName() + "\n");
                    }

                } else {
                    textView.setText("Success Error " + response.message());
                }
                hideProgressbar();
            }

            @Override
            public void onFailure(Call<CategoriesAllResponse> call, Throwable t) {
                //textView.setText("Response Error " + t.getMessage());
                hideProgressbar();
            }
        });
    }

    public void getFirstCategory(View view) {
        showProgressbar();
        storyApi.getCategory("1").enqueue(new Callback<BaseResponse<Category>>() {
            @Override
            public void onResponse(Call<BaseResponse<Category>> call, Response<BaseResponse<Category>> response) {
                if (response.isSuccessful()) {
                    String status = String.valueOf(response.body().getStatus());
                    String message = response.body().getMessage();
                    textView.setText("Response Status " + status + ": " + message + "\n");

                    textView.append("CategoryTitle: " + response.body().getData().getName() + "\n");
                } else {
                    textView.setText("Success Error " + response.message());
                    textView.append("Code " + response.code());
                }
                hideProgressbar();
            }

            @Override
            public void onFailure(Call<BaseResponse<Category>> call, Throwable t) {
                textView.setText("Response Error " + t.getMessage());
                hideProgressbar();
            }
        });
    }

    public void getCategoryStoriesWithIdOne(View view) {
        showProgressbar();
        storyApi.getStoriesByCategoryIdandUser("1").enqueue(new Callback<BaseResponse<CategoryStoriesResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<CategoryStoriesResponse>> call, Response<BaseResponse<CategoryStoriesResponse>> response) {
                if (response.isSuccessful()) {
                    String status = String.valueOf(response.body().getStatus());
                    String message = response.body().getMessage();
                    textView.setText("Response Status " + status + ": " + message + "\n");

                    for (Story s : response.body().getData().getStories()) {
                        textView.append("StoryTitle: " + s.getTitle() + "\n");
                    }

                } else {
                    textView.setText("Success Error " + response.message());
                }
                hideProgressbar();
            }

            @Override
            public void onFailure(Call<BaseResponse<CategoryStoriesResponse>> call, Throwable t) {
                textView.setText("Response Error " + t.getMessage());
                hideProgressbar();
            }
        });
    }


    public void postComment(View view) {
        showProgressbar();
        RequestBody storyId = RequestBody.create(okhttp3.MultipartBody.FORM, "1");
        RequestBody comment = RequestBody.create(okhttp3.MultipartBody.FORM, "Amazing Story");
        token = "Bearer " + Prefs.getString("Token", "");
        repository.getStoryApi().addComment(token, storyId, comment).enqueue(new Callback<BaseResponse<CommentResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<CommentResponse>> call, Response<BaseResponse<CommentResponse>> response) {
                if (response.isSuccessful()) {
                    String status = String.valueOf(response.body().getStatus());
                    String message = response.body().getMessage();
                    textView.setText("Response Status " + status + ": " + message + "\n");

                    textView.append("PostedComment: " + response.body().getData().getBody() + "\n");
                } else {
                    textView.setText("Success Error " + response.message());
                    textView.append("Code " + response.code());
                }
                hideProgressbar();
            }

            @Override
            public void onFailure(Call<BaseResponse<CommentResponse>> call, Throwable t) {
                textView.setText("Response Error " + t.getMessage());
                hideProgressbar();
            }
        });
    }

    public void bookmark(View view) {
        showProgressbar();
        token = "Bearer " + Prefs.getString("Token", "");
        repository.getStoryApi().bookmarkStory(token, 41).enqueue(new Callback<BookmarkResponse>() {
            @Override
            public void onResponse(Call<BookmarkResponse> call, Response<BookmarkResponse> response) {
                if (response.isSuccessful()) {
                    String status = String.valueOf(response.body().getStatus());
                    String message = response.body().getMessage();
                    textView.setText("Response Status " + status + ": " + message + "\n");

                    textView.append("Bookmarks: " + response.body().getData().toString() + "\n");
                } else {
                    textView.setText("Success Error " + response.message());
                    textView.append("Code " + response.code());
                }
                hideProgressbar();
            }

            @Override
            public void onFailure(Call<BookmarkResponse> call, Throwable t) {
                textView.setText("Response Error " + t.getMessage());
                hideProgressbar();
            }
        });
    }

    public void getBookmark(View view) {
        showProgressbar();
        token = "Bearer " + Prefs.getString("Token", "");
        repository.getStoryApi().getUserBookmarks(token).enqueue(new Callback<UserBookmarkResponse>() {
            @Override
            public void onResponse(Call<UserBookmarkResponse> call, Response<UserBookmarkResponse> response) {
                if (response.isSuccessful()) {
                    String status = String.valueOf(response.body().getStatus());
                    String message = response.body().getMessage();
                    textView.setText("Response Status " + status + ": " + message + "\n");

                    List<Story> data = response.body().getData();
                    for (Story s : data) {
                        textView.append("User Bookmarks: " + s.getTitle() + "\n");
                    }
                } else {
                    textView.setText("Success Error " + response.message());
                    textView.append("Code " + response.code());
                }
                hideProgressbar();
            }

            @Override
            public void onFailure(Call<UserBookmarkResponse> call, Throwable t) {
                textView.setText("Response Error " + t.getMessage());
                hideProgressbar();
            }
        });
    }

    public void showToken(View view) {
        String token = Prefs.getString("Token", "Nothing to display Ehma");
        showToast("Your token : " + token);
    }

    private void hideProgressbar() {
        progressBar.setVisibility(View.GONE);
    }

    private void showProgressbar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
