package com.project.android_kidstories.Utils;

import android.app.Application;
import android.util.Log;

import com.project.android_kidstories.Api.Api;
import com.project.android_kidstories.Api.RetrofitClient;

public class Common extends Application {
    private static final String TAG = "Common";


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: Common");
    }


}
