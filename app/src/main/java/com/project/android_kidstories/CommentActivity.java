package com.project.android_kidstories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.project.android_kidstories.Api.Api;
import com.project.android_kidstories.Api.Responses.BaseResponse;
import com.project.android_kidstories.Api.Responses.comment.CommentResponse;
import com.project.android_kidstories.Api.Responses.story.StoryBaseResponse;
import com.project.android_kidstories.Api.RetrofitClient;
import com.project.android_kidstories.Model.Comment;
import com.project.android_kidstories.sharePref.SharePref;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity {

    Api service;
    RecyclerView rv;
    EditText typeComment;
    ProgressBar commentProgressBar;
    private String token;
    private int storyId;
    private ImageView sendComment;
    private CommentAdapter adapter;
    private List<Comment> commentList;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        service = RetrofitClient.getInstance().create(Api.class);
        Toolbar addStoryToolbar = findViewById(R.id.comment_toolbar);
        setSupportActionBar(addStoryToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        rv = findViewById(R.id.comment_rv);
        typeComment = findViewById(R.id.type_comment);
        token = new SharePref(getApplicationContext()).getMyToken();
        storyId = getIntent().getIntExtra("storyId", -1);
        sendComment = findViewById(R.id.btn_send_comment);
        commentProgressBar = findViewById(R.id.comment_progress_bar);

        layoutManager = new LinearLayoutManager(CommentActivity.this, LinearLayoutManager.VERTICAL, false);

        service.getStory(storyId).enqueue(new Callback<StoryBaseResponse>() {
            @Override
            public void onResponse(Call<StoryBaseResponse> call, Response<StoryBaseResponse> response) {
                commentList = response.body().getData().getComments().getComments();
                adapter = new CommentAdapter(commentList, CommentActivity.this);
                rv.setLayoutManager(layoutManager);
                rv.setAdapter(adapter);
                commentProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<StoryBaseResponse> call, Throwable t) {
                Toast.makeText(CommentActivity.this, "Check Network Connection", Toast.LENGTH_SHORT).show();
                commentProgressBar.setVisibility(View.GONE);
            }
        });

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment();
            }
        });
    }


    public void sendComment() {
        String userComment = typeComment.getText().toString();
        if (!TextUtils.isEmpty(userComment)) {
            postComment(token, storyId, userComment);
            Toast.makeText(CommentActivity.this, "Sending Comment...", Toast.LENGTH_SHORT).show();
            sendComment.setEnabled(false);
            typeComment.setEnabled(false);
        } else {
            Toast.makeText(CommentActivity.this, "Cannot post empty comment", Toast.LENGTH_SHORT).show();
        }
    }

    public void postComment(String token, int id, String userComment) {
        // RequestBody storyId = RequestBody.create(okhttp3.MultipartBody.FORM, id);
        RequestBody comment = RequestBody.create(okhttp3.MultipartBody.FORM, userComment);
        service.addComment("Bearer " + token, comment, id).enqueue(new Callback<BaseResponse<CommentResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<CommentResponse>> call, Response<BaseResponse<CommentResponse>> response) {
                if (response.isSuccessful()) {
                    typeComment.setText("");
                    Toast.makeText(CommentActivity.this, "Comment Posted", Toast.LENGTH_SHORT).show();
                    sendComment.setEnabled(true);
                    typeComment.setEnabled(true);
                    String status = String.valueOf(response.body().getStatus());
                    String message = response.body().getMessage();
                    Log.d("Response Status ", status + ": " + message + "\n");
                    Log.d("PostedComment: ", response.body().getData().getBody() + "\n");
                    Log.d("PostedComment: ", response.body().getData().getCreatedAt() + "\n");

                    // Updates the comments with the newly added comment
                    service.getStory(id).enqueue(new Callback<StoryBaseResponse>() {
                        @Override
                        public void onResponse(Call<StoryBaseResponse> call, Response<StoryBaseResponse> response) {
                            commentList = response.body().getData().getComments().getComments();
                            adapter = new CommentAdapter(commentList, CommentActivity.this);
                            rv.setLayoutManager(layoutManager);
                            rv.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            layoutManager.scrollToPosition(commentList.size() - 1);
                        }

                        @Override
                        public void onFailure(Call<StoryBaseResponse> call, Throwable t) {
                            Toast.makeText(CommentActivity.this, "Check Network Connection", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Log.d("Success Error ", response.message());
                    Log.d("Code ", response.code() + "");
                    sendComment.setEnabled(true);
                    typeComment.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<CommentResponse>> call, Throwable t) {
                Log.e("Response Error ", t.getMessage());
                Toast.makeText(CommentActivity.this, "Check Network Connection", Toast.LENGTH_SHORT).show();
                sendComment.setEnabled(true);
                typeComment.setEnabled(true);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
