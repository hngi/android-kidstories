package com.project.android_kidstories.Api.HelperClasses;

import com.project.android_kidstories.Api.Api;
import com.project.android_kidstories.Api.Responses.BaseResponse;
import com.project.android_kidstories.Api.Responses.comment.CommentResponse;
import com.project.android_kidstories.Api.RetrofitClient;
import com.project.android_kidstories.Model.Story;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCommentHelper {
    private static boolean isNewComment =false;
    private static boolean commentAdded =false;

    public static boolean addOrUpdateComment(int storyId, String comment) {

        RequestBody commentBody = RequestBody.create(okhttp3.MultipartBody.FORM, comment);
        //RequestBody storyIdBody = RequestBody.create(MultipartBody.FORM, String.valueOf(storyId));

        if(isNewComment){
            return addComment(storyId, commentBody);
        } else {
            return addComment(storyId, commentBody);
        }

    }

    private static boolean addComment(int storyId, RequestBody comment) {
        RetrofitClient.getInstance().create(Api.class).addComment(AddStoryHelper.token, comment, storyId)
                .enqueue(new Callback<BaseResponse<CommentResponse>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<CommentResponse>> call, Response<BaseResponse<CommentResponse>> response) {
                        if(response.isSuccessful()){
                            if(response.body().getCode().equals("200")){
                                commentAdded=true;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<CommentResponse>> call, Throwable t) {
                        commentAdded=false;
                    }
                });

        return commentAdded;
    }

}
