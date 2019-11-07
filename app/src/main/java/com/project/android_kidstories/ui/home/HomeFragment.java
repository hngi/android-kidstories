package com.project.android_kidstories.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.project.android_kidstories.R;
import com.project.android_kidstories.data.model.Story;
import com.project.android_kidstories.data.source.remote.api.Api;
import com.project.android_kidstories.data.source.remote.api.RetrofitClient;
import com.project.android_kidstories.data.source.remote.response_models.bookmark.BookmarkResponse;
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

public class HomeFragment extends BaseFragment implements ExploreAdapter.OnBookmark {

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

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

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

        service = RetrofitClient.getInstance().create(Api.class);
        updateData();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (allStoriesCall != null) allStoriesCall.cancel();
            updateData();
        });

        return root;
    }

    private void toggleVisibilities(boolean isError) {
        if (isError) {
            contentView.setVisibility(View.INVISIBLE);
            errorView.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
        } else {
            contentView.setVisibility(View.VISIBLE);
            errorView.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);

        }
    }

    private void updateAdapters() {
        exploreAdapter.submitList(stories);
        // TODO: Update popular stories adapter
        populars = stories.subList(3, 14);
        popularStoriesAdapter.submitList(populars);
    }

    private void updateData() {
        swipeRefreshLayout.setRefreshing(true);

        if (allStoriesCall != null) allStoriesCall.cancel();
        String authToken = getSharePref().getUserToken();

        allStoriesCall = service.getAllStories(authToken);
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

    @Override
    public void onBookmark(int pos) {
        Story story = stories.get(pos);
        if (story.isBookmark()) {
            bookmarkStory(story.getId(), pos);
        } else {
            deleteBookmarkedStory(story.getId(), pos);
        }
    }

    private void deleteBookmarkedStory(int id, int pos) {
        String token = "Bearer " + getSharePref().getUserToken();
        service.deleteBookmarkedStory(token, id)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Story deleted from bookmarks", Toast.LENGTH_SHORT).show();
                            stories.get(pos).setBookmark(false);
                            exploreAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getContext(), "Could not delete story from bookmarks", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void bookmarkStory(int id, int pos) {
        String token = "Bearer " + getSharePref().getUserToken();
        service.bookmarkStory(token, id)
                .enqueue(new Callback<BookmarkResponse>() {
                    @Override
                    public void onResponse(Call<BookmarkResponse> call, Response<BookmarkResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Story added to bookmarks", Toast.LENGTH_SHORT).show();
                            stories.get(pos).setBookmark(true);
                            exploreAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<BookmarkResponse> call, Throwable t) {
                        Toast.makeText(getContext(), "Could not add story to bookmarks", Toast.LENGTH_SHORT).show();
                    }
                });

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