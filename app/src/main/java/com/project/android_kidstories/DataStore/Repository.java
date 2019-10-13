package com.project.android_kidstories.DataStore;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.project.android_kidstories.Api.Api;
import com.project.android_kidstories.Api.HelperClasses.AddCommentHelper;
import com.project.android_kidstories.Api.Responses.story.StoryBaseResponse;
import com.project.android_kidstories.Api.RetrofitClient;
import com.project.android_kidstories.Api.HelperClasses.AddStoryHelper;
import com.project.android_kidstories.Api.Responses.BaseResponse;
import com.project.android_kidstories.Api.Responses.Category.CategoryStoriesResponse;
import com.project.android_kidstories.Api.Responses.story.StoryAllResponse;
import com.project.android_kidstories.Model.Category;
import com.project.android_kidstories.Model.Story;
import com.project.android_kidstories.Model.User;
import com.project.android_kidstories.Utils.Common;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author .: Ehma Ugbogo
 * @email ..: ehmaugbogo@gmail.com
 * @created : 10/10/19
 */

public class Repository {
    private static final String TAG = "kidstories";
    private static Repository INSTANCE;
    private final Api api;
    private StoryDao storyDao;


    private Story story;
    private List<Story> storyList=new ArrayList<>();
    private List<Category> categoryList=new ArrayList<>();
    private Category category;

    public Repository(Context context) {
        StoryDatabase storyDatabase = StoryDatabase.getInstance(context);
        storyDao = storyDatabase.storyDao();
        //api = ((Common)context.getApplicationContext()).getApi();
        api = RetrofitClient.getInstance().create(Api.class);
        Log.d(TAG, "Repository: Created");
    }

    public Api getApi() {
        return api;
    }

    //******************** `Getters for Locally storing Stories *************************

    //TODO Rx needs to be added to reduce work on the main thread
    public Long insertOfflineStory(Story story){
        return storyDao.insertStory(story);
    }

    public void updateOfflineStory(Story data){
        storyDao.updateStory(data);
    }

    public void deleteOfflineStory(Story data){
        storyDao.deleteStory(data);
    }

    public void deleteAllOfflineStories(){
        storyDao.deleteAllStories();
    }

    public LiveData<List<Story>> getStoryList() {
        return storyDao.getAllStories();
    }




    //******************** `Getters to make Api calls *************************

    //Story APIs

    public boolean addStory(Story story, String imageUri){
        return AddStoryHelper.addOrUpdateStory(story, imageUri,true);
    }

    public void updateStory(Story newStory, String imageUri){
        AddStoryHelper.addOrUpdateStory(newStory, imageUri,false);
    }

    public Story getStory(int storyId){
        api.getStory(storyId).enqueue(new Callback<StoryBaseResponse>() {
            @Override
            public void onResponse(Call<StoryBaseResponse> call, Response<StoryBaseResponse> response) {
                if(response.isSuccessful()){
                    story = response.body().getData();
                    Log.d(TAG, "getStory Successful: ");
                }
            }

            @Override
            public void onFailure(Call<StoryBaseResponse> call, Throwable t) {
                Log.w(TAG, "onFailure: "+t.getMessage());
                story=null;
            }
        });
        return story;
    }


    public List<Story> getAllStories(){
        api.getAllStories().enqueue(new Callback<StoryAllResponse>() {
            @Override
            public void onResponse(Call<StoryAllResponse> call, Response<StoryAllResponse> response) {
                if(response.isSuccessful()){
                    storyList = response.body().getData();
                    Log.d(TAG, "getAllStories Successful: Stories "+storyList.size());
                }
            }

            @Override
            public void onFailure(Call<StoryAllResponse> call, Throwable t) {
                Log.w(TAG, "onFailure: "+t.getMessage());
            }
        });
        return storyList;
    }


    //Returns -1 on failure
    public Integer likeStory(int storyId){
        return AddStoryHelper.likeStory(storyId);
    }

    //Returns -1 on failure
    public Integer dislikeStory(int storyId){
        return AddStoryHelper.dislikeStory(storyId);
    }



    //Catergory APIs
    //******  Verified
    public void getCategory(int categoryId){
        api.getCategory(categoryId).enqueue(new Callback<BaseResponse<Category>>() {
            @Override
            public void onResponse(Call<BaseResponse<Category>> call, Response<BaseResponse<Category>> response) {
                if(response.isSuccessful()){
                    category = response.body().getData();
                    Log.d(TAG, "getCategory: Successful CategoryName "+category.getName());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Category>> call, Throwable t) {
                Log.w(TAG, "onFailure: "+t.getMessage());
                category=null;
            }
        });
    }

    public void getStoriesWithAuthourByCategoryId(int categoryId){
        api.getStoriesByCategoryIdandUser(categoryId).enqueue(new Callback<BaseResponse<CategoryStoriesResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<CategoryStoriesResponse>> call, Response<BaseResponse<CategoryStoriesResponse>> response) {
                if(response.isSuccessful()){
                    storyList = response.body().getData().getStories();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<CategoryStoriesResponse>> call, Throwable t) {
                Log.w(TAG, "onFailure: "+t.getMessage());
            }
        });
    }

    public List<Category> getAllCategories(){
        api.getAllCategories().enqueue(new Callback<BaseResponse<List<Category>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Category>>> call, Response<BaseResponse<List<Category>>> response) {
                if(response.isSuccessful()){
                    categoryList=response.body().getData();
                    Log.d(TAG, "getCategory: Successful Category "+categoryList.size());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<Category>>> call, Throwable t) {
                Log.w(TAG, "onFailure: getAllCategories"+t.getMessage());
            }
        });
        return categoryList;
    }



    //TODO Comment methods not yet rectified
    //Comment APIs
    public boolean addComment(int storyId, String comment){
        //TODO Helperclass is unfinished
        return AddCommentHelper.addOrUpdateComment(storyId,comment);
    }

    public List<Category> updateComment(int StoryId, String comment){
        return null;
    }


    public void deleteComment(String token, int commentId ){

    }






    //Authentication APIs
    public void registerUser(User user){

    }

    public void loginUser(User user){

    }

    public void logoutUser(String token){

    }

    public void getUser(String token){

    }

    public void changeUserPassword(String token, String oldPassword, String newPassword, String confirmPassword){

    }




    //***** Bookmark APIs *****//
    public void bookmarkStory(String token, int storyId){

    }

    public void deleteBookmarkedStory(String token, int storyId){

    }


    public void getStoryBookmarkStatus(String token, int storyId){

    }






    //DbUserClass APIs

    public void getAllUsers(){

    }

    public void getUserProfile(String token){

    }

    public void updateUserProfile(String token, User user){

    }

    public void updateUserProfilePicture(String token, String confirmToken, Uri photoUri){

    }



}
