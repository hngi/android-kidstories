package com.project.android_kidstories.Views.main.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.android_kidstories.DataStore.Repository;
import com.project.android_kidstories.Model.User;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<User> mUser;
    private User user;

    public ProfileViewModel() {
//        mText = new MutableLiveData<>();
//        mText.setValue("This is gallery fragment");
    }

    public void init(){

    }

    public LiveData<User> getUser() {
        return mUser;
    }


}