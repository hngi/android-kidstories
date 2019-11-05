package com.project.android_kidstories.ui.home;

import android.os.Bundle;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.project.android_kidstories.R;
import com.project.android_kidstories.data.model.Story;
import com.project.android_kidstories.ui.base.BaseFragment;
import com.project.android_kidstories.ui.home.adapters.ExploreAdapter;
import com.project.android_kidstories.ui.home.adapters.PopularStoriesAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends BaseFragment {

    private List<Story> stories = new ArrayList<>();
    private List<Story> populars = new ArrayList<>();

    private ExploreAdapter exploreAdapter;
    private PopularStoriesAdapter popularStoriesAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // This fragment needs to show a menu
        setHasOptionsMenu(true);

        // Setup the recyclerviews
        RecyclerView recyclerViewExplore = root.findViewById(R.id.recyclerview_explore_stories);
        RecyclerView recyclerViewPopularStories = root.findViewById(R.id.recyclerview_popular_stories);

        exploreAdapter = new ExploreAdapter(stories);
        popularStoriesAdapter = new PopularStoriesAdapter(populars);

        recyclerViewExplore.setAdapter(exploreAdapter);
        recyclerViewPopularStories.setAdapter(popularStoriesAdapter);

        updateData();

        return root;
    }

    private void updateData() {

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}