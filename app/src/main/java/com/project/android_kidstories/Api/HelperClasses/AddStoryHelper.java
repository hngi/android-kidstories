package com.project.android_kidstories.Api.HelperClasses;

import android.net.Uri;
import android.util.Log;

import com.pixplicity.easyprefs.library.Prefs;
import com.project.android_kidstories.Api.Api;
import com.project.android_kidstories.Api.RetrofitClient;
import com.project.android_kidstories.Api.Responses.BaseResponse;
import com.project.android_kidstories.Api.Responses.story.Reaction.ReactionResponse;
import com.project.android_kidstories.Model.Story;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddStoryHelper {
    public static final String TOKEN_KEY="token";
    private static final String TAG = "kidstories";
    public static String token = Prefs.getString(TOKEN_KEY, "");
    private static boolean isStoryAdded=false;
    private static Integer likesCount;


    public static boolean addOrUpdateStory(Story story, String imageUri, boolean isANewStory) {
        File imageFile = new File(Uri.decode(imageUri));
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile);

        MultipartBody.Part image = MultipartBody.Part.createFormData("Image", imageFile.getName(), requestFile);
        RequestBody title = RequestBody.create(okhttp3.MultipartBody.FORM, story.getTitle());
        RequestBody body = RequestBody.create(okhttp3.MultipartBody.FORM, story.getBody());
        RequestBody category = RequestBody.create(okhttp3.MultipartBody.FORM, String.valueOf(story.getCategoryId()));
        RequestBody ageInrange = RequestBody.create(okhttp3.MultipartBody.FORM, story.getAge());
        RequestBody author = RequestBody.create(okhttp3.MultipartBody.FORM, story.getTitle());
        RequestBody duration = RequestBody.create(okhttp3.MultipartBody.FORM, story.getStoryDuration());

        if(isANewStory){
            return addStory(title, body, category, ageInrange, author, duration, image);
        } else {
            return updateStory(story.getId(), title, body, category, ageInrange, author, duration, image);
        }

    }

    private static boolean addStory(RequestBody title, RequestBody body, RequestBody category, RequestBody ageInrange, RequestBody author, RequestBody duration, MultipartBody.Part image) {
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

    private static boolean updateStory(Integer id, RequestBody title, RequestBody body, RequestBody category, RequestBody ageInrange, RequestBody author, RequestBody duration, MultipartBody.Part image) {
        RetrofitClient.getInstance().create(Api.class).updateStory(token, id, title, body, category, ageInrange, author, duration, image)
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

    public static Integer likeStory(int storyId) {
        RetrofitClient.getInstance().create(Api.class).likeStory(token,storyId).enqueue(new Callback<ReactionResponse>() {
            @Override
            public void onResponse(Call<ReactionResponse> call, Response<ReactionResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response.message());
                    likesCount = response.body().getLikesCount();
                } else {
                    likesCount=-1;
                }
            }

            @Override
            public void onFailure(Call<ReactionResponse> call, Throwable t) {
                likesCount=-1;
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return likesCount;
    }

    public static Integer dislikeStory(int storyId) {
        RetrofitClient.getInstance().create(Api.class).dislikeStory(token,storyId).enqueue(new Callback<ReactionResponse>() {
            @Override
            public void onResponse(Call<ReactionResponse> call, Response<ReactionResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response.message());
                    likesCount = response.body().getLikesCount();
                } else {
                    likesCount=-1;
                }
            }

            @Override
            public void onFailure(Call<ReactionResponse> call, Throwable t) {
                likesCount=-1;
                Log.d(TAG, "onFailure: "+t.getMessage());
            }
        });
        return likesCount;
    }
}
