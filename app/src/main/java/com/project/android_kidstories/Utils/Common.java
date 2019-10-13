package com.project.android_kidstories.Utils;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

    public static boolean checkNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
