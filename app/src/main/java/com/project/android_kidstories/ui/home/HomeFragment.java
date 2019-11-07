package com.project.android_kidstories.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.project.android_kidstories.R;
import com.project.android_kidstories.data.model.Story;
import com.project.android_kidstories.data.source.remote.api.Api;
import com.project.android_kidstories.data.source.remote.api.RetrofitClient;
import com.project.android_kidstories.data.source.remote.response_models.story.StoryAllResponse;
import com.project.android_kidstories.ui.base.BaseFragment;
import com.project.android_kidstories.ui.home.adapters.ExploreAdapter;
import com.project.android_kidstories.ui.home.adapters.PopularStoriesAdapter;
import com.project.android_kidstories.ui.reading_status.ReadingStatusActivity;
import com.project.android_kidstories.ui.settings.SettingsActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends BaseFragment {

    private List<Story> stories = new ArrayList<>();
    private List<Story> populars = new ArrayList<>();

    private ExploreAdapter exploreAdapter;
    private PopularStoriesAdapter popularStoriesAdapter;

    private Api service;
    private Call<StoryAllResponse> allStoriesCall;

    private String currentPage = "1";

    private View errorView;
    private View contentView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // This fragment needs to show a menu
        setHasOptionsMenu(true);

        errorView = root.findViewById(R.id.error_msg);
        contentView = root.findViewById(R.id.content_fragment_home);

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

        exploreAdapter = new ExploreAdapter(requireContext());
        popularStoriesAdapter = new PopularStoriesAdapter(requireContext());

        recyclerViewExplore.setAdapter(exploreAdapter);
        recyclerViewPopularStories.setAdapter(popularStoriesAdapter);

        service = RetrofitClient.getInstance().create(Api.class);
        updateData();

        return root;
    }

    private void toggleVisibilities(boolean isError) {
        if (isError) {
            contentView.setVisibility(View.INVISIBLE);
            errorView.setVisibility(View.VISIBLE);
        } else {
            contentView.setVisibility(View.VISIBLE);
            errorView.setVisibility(View.GONE);
        }
    }

    private void updateAdapters() {
        exploreAdapter.submitList(stories);
        // TODO: Update popular stories adapter
        populars = stories.subList(3, 14);
        popularStoriesAdapter.submitList(populars);
    }

    private void updateData() {
        if (allStoriesCall != null) allStoriesCall.cancel();
        String authToken = getSharePref().getUserToken();

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

    /*@Override
    public void onStorySearched(String query) {
        exploreAdapter.getFilter().filter(query);
    }*/

    public class StoryComparator implements Comparator<Story> {

        @Override
        public int compare(Story story1, Story story2) {


            int story1PriorityCount = story1.getDislikesCount() + story1.getLikesCount();
            int story2PriorityCount = story2.getDislikesCount() + story2.getLikesCount();

            return Integer.compare(story1PriorityCount, story2PriorityCount);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (allStoriesCall != null) allStoriesCall.cancel();
    }
}