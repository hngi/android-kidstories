package com.project.android_kidstories.ui.categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.project.android_kidstories.Api.Api;
import com.project.android_kidstories.Api.Responses.Category.CategoriesAllResponse;
import com.project.android_kidstories.Api.RetrofitClient;
import com.project.android_kidstories.R;
import com.project.android_kidstories.data.model.Category;
import com.project.android_kidstories.ui.base.BaseFragment;
import com.project.android_kidstories.ui.categories.adapters.RecyclerCategoryAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;


public class CategoriesFragment extends BaseFragment {

    public static CategoriesFragment newInstance() {
        return new CategoriesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_categories, container, false);

        getCategories(root);

        return root;
    }

    private void getCategories(View root) {
        View progressBar = root.findViewById(R.id.category_bar);
        progressBar.setVisibility(View.VISIBLE);

        /*Create handle for the RetrofitInstance interface*/
        Api service = RetrofitClient.getInstance().create(Api.class);
        Call<CategoriesAllResponse> categories = service.getAllCategories();

        categories.enqueue(new Callback<CategoriesAllResponse>() {
            @Override
            public void onResponse(Call<CategoriesAllResponse> call, Response<CategoriesAllResponse> response) {
                progressBar.setVisibility(View.GONE);
                RecyclerView recyclerView = root.findViewById(R.id.category_recycler);

                if (response.isSuccessful()) {
                    CategoriesAllResponse allResponse = response.body();
                    if (allResponse == null) {
                        showSnack(root, requireActivity().getString(R.string.no_category_received));
                        return;
                    }
                    List<Category> categoryList = allResponse.getData();

                    RecyclerCategoryAdapter adapter = new RecyclerCategoryAdapter(getContext(), categoryList);

                    int spanCount;
                    try {
                        spanCount = requireContext().getResources().getInteger(R.integer.home_fragment_gridspan);
                    } catch (NullPointerException e) {
                        spanCount = 1;
                    }
                    GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<CategoriesAllResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }
}
