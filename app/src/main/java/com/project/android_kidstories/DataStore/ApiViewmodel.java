package com.project.android_kidstories.DataStore;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

/**
 * @author .: Ehma Ugbogo
 * @email ..: ehmaugbogo@gmail.com
 * @created : 12/10/19
 */


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
