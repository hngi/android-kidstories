package com.project.android_kidstories.DataStore;

import android.app.Application;
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
    //private UserDao userDao;


    public static synchronized Repository getInstance(Application application){
        if(INSTANCE==null){
            return new Repository(application);
        }
        return INSTANCE;
    }


    public Repository(Context context) {
        StoryDatabase storyDatabase = StoryDatabase.getInstance(context);
        storyDao = storyDatabase.storyDao();

        //userDao = storyDatabase.userDao();
//        api = ((Common)context.getApplicationContext()).getStoryApi();
        api = RetrofitClient.getInstance().create(Api.class);
        Log.d(TAG, "Repository: Created");
    }

    public Api getStoryApi() {
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

    //Getters for User
//    public Long insertUser(User user){
//       return userDao.insertUser(user);
//    }

//    public void updateUser(User user){
//        userDao.updateUser(user);
//    }
//
//    public void deleteUser(User user){
//        userDao.deleteUser(user);
//    }
//
//    public List<User> getAllLocalUsers(){
//        return userDao.getallUsers();
//    }


    //******************** `Getters to make Api calls *************************

    //Story APIs

    public boolean addStory(Story story, String imageUri){
        return AddStoryHelper.addOrUpdateStory(story, imageUri,true);
    }

    public void updateStory(Story newStory, String imageUri){
        AddStoryHelper.addOrUpdateStory(newStory, imageUri,false);
    }


}
