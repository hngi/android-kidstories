package com.project.android_kidstories;

import android.content.ContextWrapper;
import android.os.Bundle;
import android.util.Log;

import com.pixplicity.easyprefs.library.Prefs;
import com.project.android_kidstories.Api.Api;
import com.project.android_kidstories.Api.HelperClasses.AddStoryHelper;
import com.project.android_kidstories.Api.Responses.story.StoryAllResponse;
import com.project.android_kidstories.Api.RetrofitClient;
import com.project.android_kidstories.DataStore.ApiViewmodel;
import com.project.android_kidstories.DataStore.Repository;
import com.project.android_kidstories.Model.Category;
import com.project.android_kidstories.Model.Story;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "android_kidstories";
    private List<Story> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




    }





}
