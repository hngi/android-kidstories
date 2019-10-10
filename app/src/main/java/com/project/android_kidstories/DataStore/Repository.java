package com.project.android_kidstories.DataStore;

import android.content.Context;
import android.util.Log;

import com.project.android_kidstories.Api.ApiInterface;
import com.project.android_kidstories.Api.Client;
import com.project.android_kidstories.Api.Responses.BaseResponse;
import com.project.android_kidstories.Api.Responses.CategoryAllResponse;
import com.project.android_kidstories.Api.Responses.StoryCategoryResponse;
import com.project.android_kidstories.Model.Category;
import com.project.android_kidstories.Model.Story;
import com.project.android_kidstories.Model.User;

import java.util.List;

import androidx.lifecycle.LiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {
    private static final String TAG = "Repository";
    private final ApiInterface api;
    private StoryDao storyDao;

    private List<Category> categories;


    public Repository(Context context) {
        StoryDatabase storyDatabase = StoryDatabase.getInstance(context);
        storyDao = storyDatabase.storyDao();
        api = Client.getInstance().create(ApiInterface.class);
    }

    //`Getters for bookmarking Stories
    //TODO Rx needs to be added to remove working on the main thread
    public Long insertStory(Story story){
        return storyDao.insertStory(story);
    }

    public void updateStory(Story data){
        storyDao.updateStory(data);
    }

    public void deleteStory(Story data){
        storyDao.deleteStory(data);
    }

    public void deleteAllStories(){
        storyDao.deleteAllStories();
    }

    public LiveData<List<Story>> getStoryList() {
        return storyDao.getAllStories();
    }


    //`Getters to make Api calls


    public List<Category> getAllCategories(){
        return categories;
    }

    public List<Story> getCategoryStories(String categoryId){

        return null;
    }

    public List<Story> getAllStories(){
        return null;
    }

    public Story getStory(int storyId){
        return null;
    }

    public void addStory(Story newStory){

    }


    public void registerUser(User user){

    }

    public void loginUser(User user){

    }

    public void getUserProfile(String token){

    }

    public void reactToStory(String action, String storyId){

    }





}
