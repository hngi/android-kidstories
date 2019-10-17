package com.project.android_kidstories;


import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.project.android_kidstories.Api.Api;
import com.project.android_kidstories.Api.Responses.BaseResponse;
import com.project.android_kidstories.Api.Responses.Category.CategoryStoriesResponse;
import com.project.android_kidstories.DataStore.Repository;
import com.project.android_kidstories.R;
import com.project.android_kidstories.adapters.RecyclerCategoryStoriesAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryListingActivity extends AppCompatActivity {

    private Repository repository;
    private Api storyApi;
    ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private RecyclerCategoryStoriesAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_listing);

        String categoryName = getIntent().getStringExtra("categoryName");
        int categoryId = getIntent().getIntExtra("categoryId", -1);

        repository = Repository.getInstance(this.getApplication());
        storyApi = repository.getStoryApi();

        CollapsingToolbarLayout toolbar = findViewById(R.id.Collapse_toolbar);
        Toolbar toolbar1 = findViewById(R.id.toolbar);
        toolbar.setTitle(categoryName);
        setSupportActionBar(toolbar1);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        getCategoryStoriesWithId(categoryId);

    }

    public void getCategoryStoriesWithId(int id) {
        String idString = Integer.toString(id);

        storyApi.getStoriesByCategoryIdandUser(idString).enqueue(new Callback<BaseResponse<CategoryStoriesResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<CategoryStoriesResponse>> call, Response<BaseResponse<CategoryStoriesResponse>> response) {

                recyclerView = findViewById(R.id.rv_list);
                if (response.isSuccessful()) {

                    adapter = new RecyclerCategoryStoriesAdapter(StoryListingActivity.this,response.body().getData().getStories() );
                    GridLayoutManager layoutManager = new GridLayoutManager(StoryListingActivity.this, 1);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);

                    progressDialog.hide();
//

                }else {
                    // textView.setText("Success Error " +response.message());
                }

            }

            @Override
            public void onFailure(Call<BaseResponse<CategoryStoriesResponse>> call, Throwable t) {
                progressDialog.hide();
                //textView.setText("Response Error " + t.getMessage());
            }
        });
    }

}
