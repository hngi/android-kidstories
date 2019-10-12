package com.project.android_kidstories.DataStore;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class ApiViewmodel extends AndroidViewModel {

    private final Repository repository;

    public ApiViewmodel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
    }

    public Repository getRepository() {
        return repository;
    }
}
