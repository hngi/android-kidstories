package com.project.android_kidstories.Views;

import android.content.ContextWrapper;
import android.os.Bundle;
import android.util.Log;

import com.pixplicity.easyprefs.library.Prefs;
import com.project.android_kidstories.Api.HelperClasses.AddStoryHelper;
import com.project.android_kidstories.BaseActivity;
import com.project.android_kidstories.DataStore.Repository;
import com.project.android_kidstories.R;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPrefAndsaveToken();



        Repository repository=Repository.getInstance(getApplicationContext());


        int size = repository.getAllCategories().size();
        int size1 = repository.getAllStories().size();

        Log.d(TAG, "onCreate: Category Size: "+size);
        Log.d(TAG, "onCreate: Story Size: "+size1);
    }




    private void initPrefAndsaveToken(){
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();

        Prefs.putString(AddStoryHelper.TOKEN_KEY,"aTokenStringShouldBeHere");
    }
}
