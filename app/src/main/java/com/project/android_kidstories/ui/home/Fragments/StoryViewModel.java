package com.project.android_kidstories.ui.home.Fragments;

import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.project.android_kidstories.Api.Api;
import com.project.android_kidstories.Api.Responses.Category.CategoriesAllResponse;
import com.project.android_kidstories.Api.Responses.story.StoryAllResponse;
import com.project.android_kidstories.Api.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoryViewModel extends ViewModel {

    private Api service;
    public String token;
    private MutableLiveData<StoryAllResponse> _response = new MutableLiveData<>();
    private MutableLiveData<CategoriesAllResponse> _responseCategories = new MutableLiveData<>();


    public LiveData<CategoriesAllResponse> getCategories(){
        service = RetrofitClient.getInstance().create(Api.class);
        Call<CategoriesAllResponse> categories = service.getAllCategories();
        categories.enqueue(new Callback<CategoriesAllResponse>() {
            @Override
            public void onResponse(Call<CategoriesAllResponse> call, Response<CategoriesAllResponse> response) {

                Log.d("XXX cat success", String.valueOf(response.isSuccessful()));
                Log.d("XXX cat body", String.valueOf(response.body().getData()));

                if (response.isSuccessful()) {


                    _responseCategories.postValue(response.body());

                }
            }
                @Override
                public void onFailure(Call<CategoriesAllResponse> call, Throwable t) {
//                progressBar.setVisibility(View.INVISIBLE);

                }
            });

       return _responseCategories;

        }

    public LiveData<StoryAllResponse> fetchStories(String page){
        /*Create handle for the RetrofitInstance interface*/
        service = RetrofitClient.getInstance().create(Api.class);
        Call<StoryAllResponse> stories = service.getAllStoriesWithAuth(token, page);
//        Log.i("apple", "Size: " + categories.isExecuted());

        stories.enqueue(new Callback<StoryAllResponse>() {
            @Override
            public void onResponse(Call<StoryAllResponse> call, Response<StoryAllResponse> response) {

                Log.d("XXXX", response.message());

                if (response.isSuccessful()) {

                    Log.d("XXXX", response.body().getStories().toString());

                    _response.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<StoryAllResponse> call, Throwable t) {

            }


        });
        return _response;
    }
}
