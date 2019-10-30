package com.project.android_kidstories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.android_kidstories.Api.Api;
import com.project.android_kidstories.Api.Responses.BaseResponse;
import com.project.android_kidstories.Api.Responses.comment.CommentResponse;
import com.project.android_kidstories.Api.RetrofitClient;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity {

    Api service;
    RecyclerView rv;
    EditText typeComment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        service = RetrofitClient.getInstance().create(Api.class);

        rv = findViewById(R.id.comment_rv);
        typeComment=findViewById(R.id.type_comment);

        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        CommentAdapter adapter = new CommentAdapter(SingleStoryActivity.returnComments(),this);
        rv.setLayoutManager(manager);
        rv.setAdapter(adapter);
    }


    public void sendComment(View view) {
        String token = "" ;
        String id = "";
        String userComment = typeComment.getText().toString();
        postComment(token,id,userComment);
    }

    public void postComment(String token,String id, String userComment){
        RequestBody storyId = RequestBody.create(okhttp3.MultipartBody.FORM, id);
        RequestBody comment = RequestBody.create(okhttp3.MultipartBody.FORM, userComment);
        service.addComment(token, storyId, comment).enqueue(new Callback<BaseResponse<CommentResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<CommentResponse>> call, Response<BaseResponse<CommentResponse>> response) {
                if (response.isSuccessful()) {
                    String status = String.valueOf(response.body().getStatus());
                    String message = response.body().getMessage();
                    Log.e("Response Status ", status + ": " + message + "\n");

                   Log.e("PostedComment: ", response.body().getData().getBody() + "\n");
                } else {
                    Log.e("Success Error ", response.message());
                   Log.e("Code ", response.code()+"");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<CommentResponse>> call, Throwable t) {
                Log.e("Response Error ", t.getMessage());
            }
        });
    }
}
