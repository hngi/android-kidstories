package com.project.android_kidstories.Utils;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.pixplicity.easyprefs.library.Prefs;
import com.project.android_kidstories.Api.HelperClasses.AddStoryHelper;

public class Common extends Application {
    private static final String TAG = "Common";


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: Common");

        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

    }

    public static boolean checkNetwork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void saveToken(String token) {
        Prefs.putString(AddStoryHelper.TOKEN_KEY, token);
    }

    private String getSavedToken(String token) {
        return Prefs.getString(AddStoryHelper.TOKEN_KEY, "aTokenStringShouldBeHere");
    }



}
