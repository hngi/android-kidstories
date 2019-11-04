package com.project.android_kidstories.viewModel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.project.android_kidstories.DataStore.Repository;
import com.project.android_kidstories.data.model.User;

import java.util.List;

public class UserViewModel extends AndroidViewModel {
    private final Repository repository;
    public UserViewModel(@NonNull Application application, Repository repository) {
        super(application);
        this.repository = repository;
    }

    public Long insertUser(User user){
        return repository.insertUser(user);
    }

    public void updateUser(User user){
        repository.updateUser(user);
    }

    public void deleteUser(User user){
        repository.deleteUser(user);
    }

    public List<User> getallUsers(){
        return repository.getallUsers();
    }
}
