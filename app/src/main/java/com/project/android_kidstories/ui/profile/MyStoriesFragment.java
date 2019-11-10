package com.project.android_kidstories.ui.profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.project.android_kidstories.R;
import com.project.android_kidstories.data.Repository;
import com.project.android_kidstories.data.model.Story;
import com.project.android_kidstories.data.source.remote.response_models.story.StoryAllResponse;
import com.project.android_kidstories.ui.base.BaseFragment;
import com.project.android_kidstories.ui.categories.adapters.CategoryTabAdapter;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class MyStoriesFragment extends BaseFragment {

    private int userId;
    private List<Story> storyList = new ArrayList<>();
    private Context context;

    private Repository repository;
    private ProgressBar progressBar;
    private View errorView;

    SwipeRefreshLayout swipeRefreshLayout;

    private Call<StoryAllResponse> allStoriesCall;

    public static MyStoriesFragment getInstance() {
        return new MyStoriesFragment();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_my_stories, container, false);
        progressBar = root.findViewById(R.id.loading_bar);
        errorView = root.findViewById(R.id.error_msg);

        swipeRefreshLayout = root.findViewById(R.id.swiper);

        context = requireContext();
        userId = getSharePref().getUserId();

        if (userId == -1) {
            showToast("No user logged in");
        }

        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (allStoriesCall != null) allStoriesCall.cancel();
            fetchStories(root, "1");
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        repository = new Repository(getActivity().getApplicationContext());
        fetchStories(view, "1");
    }

    private void fetchStories(View view, String page) {
        swipeRefreshLayout.setRefreshing(true);

        allStoriesCall = repository.getStoryApi().getAllStories(page);
        allStoriesCall.enqueue(new Callback<StoryAllResponse>() {
            @Override
            public void onResponse(Call<StoryAllResponse> call, Response<StoryAllResponse> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful()) {
                    storyList.clear();
                    for (int i = 0; i < response.body().getStories().size(); i++) {
                        if (response.body().getStories().get(i).getUserId() == getSharePref().getUserId()) {
                            Story thisStory = response.body().getStories().get(i);
                            storyList.add(thisStory);
                        }
                    }

                    updateViews(view);
                }
            }

            @Override
            public void onFailure(Call<StoryAllResponse> call, Throwable t) {
                showSnack(view, "Can't retrieve your stories, check connectivity and try again");
                progressBar.setVisibility(View.GONE);
                errorView.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void updateViews(View root) {
        RecyclerView recyclerView = root.findViewById(R.id.my_story_recyclerView);
        TextView userHasNoStory = root.findViewById(R.id.error_message);

        CategoryTabAdapter adapter = new CategoryTabAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        progressBar.setVisibility(View.GONE);
        if (storyList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            userHasNoStory.setVisibility(View.VISIBLE);
        } else {
            userHasNoStory.setVisibility(View.GONE);
        }

        adapter.submitList(storyList);
    }

}
