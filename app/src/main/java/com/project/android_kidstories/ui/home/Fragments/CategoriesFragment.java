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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
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
    StoryViewModel storyViewModel;
    private ProgressBar progressBar;

    public static CategoriesFragment newInstance() {
        return new CategoriesFragment();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_categories, container, false);

        progressBar = v.findViewById(R.id.category_bar);

        recyclerView = v.findViewById(R.id.category_recycler);

        storyViewModel = ViewModelProviders.of(this).get(StoryViewModel.class);
        getCategories();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    private void getCategories(){
        Observer<CategoriesAllResponse> observe = categoriesAllResponse -> {
            adapter = new RecyclerCategoryAdapter(getContext(), categoriesAllResponse);
            int spanCount;
            try {
                spanCount = getContext().getResources().getInteger(R.integer.home_fragment_gridspan);
            } catch (NullPointerException e) {
                spanCount = 1;
            }
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        };
        storyViewModel.getCategories().observe(this, observe);

    }
}
