package com.project.android_kidstories.ui.categories;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.project.android_kidstories.R;
import com.project.android_kidstories.data.model.Category;
import com.project.android_kidstories.data.source.remote.api.Api;
import com.project.android_kidstories.data.source.remote.api.RetrofitClient;
import com.project.android_kidstories.data.source.remote.response_models.category.CategoriesAllResponse;
import com.project.android_kidstories.ui.base.BaseFragment;
import com.project.android_kidstories.ui.categories.adapters.CategoriesPagerAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;


public class CategoriesFragment extends BaseFragment {

    public static CategoriesFragment newInstance() {
        return new CategoriesFragment();
    }

    private List<Category> categories = new ArrayList<>();
    private View progressBar;
    private View errorView;

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_categories, container, false);

        tabLayout = root.findViewById(R.id.tabLayout_categories_fragment);
        viewPager = root.findViewById(R.id.viewPager_categories_fragment);

        progressBar = root.findViewById(R.id.category_bar);
        errorView = root.findViewById(R.id.error_msg);

        getCategoriesList();

        return root;
    }

    private void updateViews() {
        tabLayout.setVisibility(View.VISIBLE);

        // Setup ViewPager
        CategoriesPagerAdapter pagerAdapter = new CategoriesPagerAdapter(getChildFragmentManager());

        // Populate
        for (Category category : categories) {
            Log.d("GLOBAL_SCOPE", category.getName());

        }

    }

    private void getCategoriesList() {
        progressBar.setVisibility(View.VISIBLE);

        Api service = RetrofitClient.getInstance().create(Api.class);
        Call<CategoriesAllResponse> allResponseCall = service.getAllCategories();

        allResponseCall.enqueue(new Callback<CategoriesAllResponse>() {
            @Override
            public void onResponse(Call<CategoriesAllResponse> call, Response<CategoriesAllResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    errorView.setVisibility(View.GONE);
                    CategoriesAllResponse allResponse = response.body();
                    if (allResponse == null) {
                        errorView.setVisibility(View.VISIBLE);
                    } else {
                        categories = allResponse.getData();
                        updateViews();
                    }
                } else {
                    errorView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<CategoriesAllResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                errorView.setVisibility(View.VISIBLE);
            }
        });
    }
}
