package com.project.android_kidstories.data;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import androidx.lifecycle.LiveData;
import com.project.android_kidstories.data.model.ReadStory;
import com.project.android_kidstories.data.model.Story;
import com.project.android_kidstories.data.model.User;
import com.project.android_kidstories.data.source.local.relational.ReadStoryDao;
import com.project.android_kidstories.data.source.local.relational.StoryDao;
import com.project.android_kidstories.data.source.local.relational.StoryDatabase;
import com.project.android_kidstories.data.source.local.relational.UserDao;
import com.project.android_kidstories.data.source.remote.api.Api;
import com.project.android_kidstories.data.source.remote.api.RetrofitClient;

import java.util.List;

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
    private UserDao userDao;
    private ReadStoryDao readStoryDao;

    public Repository(Context context) {
        StoryDatabase storyDatabase = StoryDatabase.getInstance(context);
        storyDao = storyDatabase.storyDao();
        readStoryDao = storyDatabase.readStoryDao();
        userDao = storyDatabase.userDao();

        //userDao = storyDatabase.userDao();
//        api = ((Common)context.getApplicationContext()).getStoryApi();
        api = RetrofitClient.getInstance().create(Api.class);
        Log.d(TAG, "Repository: Created");
    }

    public LiveData<ReadStory> getStoryForId(String id) {
        return readStoryDao.getStoryForId(id);
    }
    public LiveData<User>getUserId(String id){return getUserId(id);}

    public void insertReadStoryId(ReadStory readStory) {
        try {
            readStoryDao.insertReadStory(readStory);
        } catch (Exception e) {

        }
    }

    public static synchronized Repository getInstance(Application application) {
        if (INSTANCE == null) {
            return new Repository(application);
        }
        return INSTANCE;
    }

    public Api getStoryApi() {
        return api;
    }

    public Api getUserProfileApi() {
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
    public Long insertUser(User user){
       return userDao.insertUser(user);
    }

    public void updateUser(User user){
        userDao.updateUser(user);
    }

    public void deleteUser(User user){
        userDao.deleteUser(user);
    }

    public List<User> getallUsers(){
        return userDao.getallUsers();
    }


    //******************** `Getters to make Api calls *************************

    //Story APIs

//    public boolean addStory(Story story, String imageUri){
//        return AddStoryHelper.addOrUpdateStory(story, imageUri,true);
//    }
//
//    public void updateStory(Story newStory, String imageUri){
//        AddStoryHelper.addOrUpdateStory(newStory, imageUri,false);
//    }


}
