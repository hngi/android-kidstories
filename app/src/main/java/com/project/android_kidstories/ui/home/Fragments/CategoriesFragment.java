package com.project.android_kidstories.ui.home.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.project.android_kidstories.Api.Api;
import com.project.android_kidstories.Api.Responses.Category.CategoriesAllResponse;
import com.project.android_kidstories.Api.RetrofitClient;
import com.project.android_kidstories.R;
import com.project.android_kidstories.adapters.RecyclerCategoryAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CategoriesFragment extends Fragment {
    private RecyclerCategoryAdapter adapter;
    RecyclerView recyclerView;
    private ProgressBar progressBar;

    public static CategoriesFragment newInstance() {
        return new CategoriesFragment();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_categories, container, false);
       /* recyclerView = v.findViewById(R.id.category_recycler);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);*/

        // Glide.with(this).load("http://i.imgur.com/DvpvklR.png").into(imageView);

       /* RecyclerStoriesAdapter recyclerAdapter = new RecyclerStoriesAdapter(getContext(), images, authors);
        recyclerView.setAdapter(recyclerAdapter);*/

        progressBar = v.findViewById(R.id.category_bar);

        progressBar.setVisibility(View.VISIBLE);

        /*Create handle for the RetrofitInstance interface*/
        Api service = RetrofitClient.getInstance().create(Api.class);
        Call<CategoriesAllResponse> categories = service.getAllCategories();
        Log.i("apple", "Size: " + categories.isExecuted());

        categories.enqueue(new Callback<CategoriesAllResponse>() {
            @Override
            public void onResponse(Call<CategoriesAllResponse> call, Response<CategoriesAllResponse> response) {
                //  generateCategoryList(response.body(),v);
                progressBar.setVisibility(View.GONE);
                recyclerView = v.findViewById(R.id.category_recycler);

                if (response.isSuccessful()) {
                    adapter = new RecyclerCategoryAdapter(getContext(), response.body());
                    //LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    int spanCount;
                    try {
                        spanCount = getContext().getResources().getInteger(R.integer.home_fragment_gridspan);
                    } catch (NullPointerException e) {
                        spanCount = 1;
                    }
                    GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                } else {

                }
            }

            @Override
            public void onFailure(Call<CategoriesAllResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);

            }
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    /*Method to generate List of data using RecyclerView with custom adapter*/
    private void generateCategoryList(CategoriesAllResponse categoryList, View view) {
        recyclerView = view.findViewById(R.id.category_recycler);
        adapter = new RecyclerCategoryAdapter(getContext(), categoryList);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

}
