package com.project.android_kidstories.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.*;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.project.android_kidstories.R;
import com.project.android_kidstories.data.model.Story;
import com.project.android_kidstories.data.source.remote.api.Api;
import com.project.android_kidstories.data.source.remote.api.RetrofitClient;
import com.project.android_kidstories.data.source.remote.response_models.story.StoryAllResponse;
import com.project.android_kidstories.ui.MainActivity;
import com.project.android_kidstories.ui.base.BaseFragment;
import com.project.android_kidstories.ui.categories.CategoriesFragment;
import com.project.android_kidstories.ui.home.adapters.ExploreAdapter;
import com.project.android_kidstories.ui.home.adapters.PopularStoriesAdapter;
import com.project.android_kidstories.ui.reading_status.ReadingStatusActivity;
import com.project.android_kidstories.ui.settings.SettingsActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends BaseFragment {

    private static final int NUM_POPULAR_STORIES = 10;

    private List<Story> stories = new ArrayList<>();
    private List<Story> populars = new ArrayList<>();

    private ExploreAdapter exploreAdapter;
    private PopularStoriesAdapter popularStoriesAdapter;

    private Api service;
    private Call<StoryAllResponse> allStoriesCall;

    private String currentPage = "1";

    private SwipeRefreshLayout swipeRefreshLayout;

    private View errorView;
    private View contentView;

    private MainActivity mainActivity;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        // Update Activity's toolbar title
        try {
            ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("Stories");
        } catch (NullPointerException npe) {
            Log.d("GLOBAL_SCOPE", "Can't set toolbar title");
        }

        // This fragment needs to show a menu
        setHasOptionsMenu(true);

        errorView = root.findViewById(R.id.error_msg);
        contentView = root.findViewById(R.id.content_fragment_home);
        swipeRefreshLayout = root.findViewById(R.id.swiper);

        // Setup the recyclerviews
        RecyclerView recyclerViewExplore = root.findViewById(R.id.recyclerview_explore_stories);
        RecyclerView recyclerViewPopularStories = root.findViewById(R.id.recyclerview_popular_stories);

        // Disable vertical scrolling in recyclerViewExplore
        recyclerViewExplore.setLayoutManager(new LinearLayoutManager(requireContext()) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        exploreAdapter = new ExploreAdapter(this);
        popularStoriesAdapter = new PopularStoriesAdapter(requireContext());

        recyclerViewExplore.setAdapter(exploreAdapter);
        recyclerViewPopularStories.setAdapter(popularStoriesAdapter);

        new LinearSnapHelper().attachToRecyclerView(recyclerViewPopularStories);

        service = RetrofitClient.getInstance().create(Api.class);

        mainActivity = (MainActivity) requireActivity();
        stories = mainActivity.getHomeStories();
        if (stories.isEmpty()) {
            updateData();
        } else {
            Log.d("GLOBAL_SCOPE", String.valueOf(stories.size()));
            toggleVisibilities(false);
            updateAdapters();
        }

        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (allStoriesCall != null) allStoriesCall.cancel();
            updateData();
        });

        TextView viewAll = root.findViewById(R.id.txt_viewall_home);
        viewAll.setOnClickListener(v -> ((MainActivity) getActivity()).navigateToFragment(new CategoriesFragment(), "All Stories"));

        return root;
    }

    private void toggleVisibilities(boolean isError) {
        if (isError) {
            contentView.setVisibility(View.GONE);
            errorView.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
        } else {
            contentView.setVisibility(View.VISIBLE);
            errorView.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);

        }
    }

    private void updateAdapters() {
        int firstStoryId = stories.get(0).getId();
        int lastStoryId = stories.get(stories.size() - 1).getId();

        if (firstStoryId < lastStoryId) {
            Collections.reverse(stories);
        }

        exploreAdapter.submitList(stories);

        // Get the stories with most engagement (likes + dislikes)
        new Handler().post(() -> {
            populars.addAll(stories);
            Collections.sort(populars, new StoryComparator());
            populars = populars.subList(0, NUM_POPULAR_STORIES);
            popularStoriesAdapter.submitList(populars);
        });
    }

    private void updateData() {
        swipeRefreshLayout.setRefreshing(true);

        if (allStoriesCall != null) allStoriesCall.cancel();
        String authToken = "Bearer " + getSharePref().getUserToken();

        allStoriesCall = service.getAllStoriesWithAuth(authToken, "1");
        allStoriesCall.enqueue(new Callback<StoryAllResponse>() {
            @Override
            public void onResponse(Call<StoryAllResponse> call, Response<StoryAllResponse> response) {
                if (response.isSuccessful()) {
                    toggleVisibilities(false);

                    StoryAllResponse storyAllResponse = response.body();
                    if (storyAllResponse == null) {
                        toggleVisibilities(true);
                        return;
                    }

                    stories = storyAllResponse.getStories();
                    updateAdapters();
                    mainActivity.setHomeStrories(stories);

                } else {
                    toggleVisibilities(true);
                }
            }

            @Override
            public void onFailure(Call<StoryAllResponse> call, Throwable t) {
                // Show Error
                toggleVisibilities(true);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_streaks:
                startActivity(new Intent(requireContext(), ReadingStatusActivity.class));
                break;

            case R.id.action_settings:
                startActivity(new Intent(requireContext(), SettingsActivity.class));
                break;

            default:
                return false;
        }

        return true;
    }

/*

    private void onStorySearched(String query) {

    }
*/

    public class StoryComparator implements Comparator<Story> {

        @Override
        public int compare(Story story1, Story story2) {

            int story1PriorityCount = story1.getDislikesCount() + story1.getLikesCount();
            int story2PriorityCount = story2.getDislikesCount() + story2.getLikesCount();

            return Integer.compare(story2PriorityCount, story1PriorityCount);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (allStoriesCall != null) allStoriesCall.cancel();
    }
}