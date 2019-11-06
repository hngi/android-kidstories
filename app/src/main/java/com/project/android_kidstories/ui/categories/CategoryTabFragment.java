package com.project.android_kidstories.ui.categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.project.android_kidstories.R;
import com.project.android_kidstories.ui.home.adapters.ExploreAdapter;

public class CategoryTabFragment extends Fragment {

    private String category_name;

    private ExploreAdapter adapter;

    public CategoryTabFragment(String category_name) {
        this.category_name = category_name;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_category_tab, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.recyclerview_category_tab);
        adapter = new ExploreAdapter(requireContext());

        recyclerView.setAdapter(adapter);

        getCategoryStories();

        return root;
    }

    private void getCategoryStories() {

    }
}
