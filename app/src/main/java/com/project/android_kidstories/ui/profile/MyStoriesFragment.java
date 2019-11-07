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
import com.project.android_kidstories.R;
import com.project.android_kidstories.data.Repository;
import com.project.android_kidstories.data.model.Story;
import com.project.android_kidstories.data.source.remote.response_models.story.StoryAllResponse;
import com.project.android_kidstories.ui.base.BaseFragment;
import com.project.android_kidstories.ui.profile.adapters.MyStoriesAdapter;
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

    public static MyStoriesFragment getInstance() {
        return new MyStoriesFragment();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_my_stories, container, false);
        progressBar = root.findViewById(R.id.loading_bar);
        errorView = root.findViewById(R.id.error_msg);

        context = requireContext();
        userId = getSharePref().getUserId();

        if (userId == -1) {
            showToast("No user logged in");
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

/*<<<<<<< HEAD
        Repository repository = new Repository(context);
        repository.getStoryApi().getAllStories("1").enqueue(new Callback<StoryAllResponse>() {
            @Override
            public void onResponse(Call<StoryAllResponse> call, Response<StoryAllResponse> response) {
                if (response.isSuccessful()) {
                    StoryAllResponse storyAllResponse = response.body();
                    if (storyAllResponse == null) {
                        // Nothing was received
                        showSnack(view, "No story received");
                        progressBar.setVisibility(View.GONE);
                        errorView.setVisibility(View.VISIBLE);
                        return;
                    }
                    List<Story> stories = storyAllResponse.getData();
                    for (int i = 0; i < stories.size(); i++) {
                        if (stories.get(i).getUserId() == userId) {
                            storyList.add(stories.get(i));*/

        repository = new Repository(getActivity().getApplicationContext());
        fetchStories(view, "1");
    }

    void fetchStories(View view, String page) {
        repository.getStoryApi().getAllStories(page).enqueue(new Callback<StoryAllResponse>() {
            @Override
            public void onResponse(Call<StoryAllResponse> call, Response<StoryAllResponse> response) {
                if (response.isSuccessful()) {
                    for (int i = 0; i < response.body().getStories().size(); i++) {
                        if (response.body().getStories().get(i).getUserId() == getSharePref().getUserId()) {
                            storyList.add(response.body().getStories().get(i));
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
            }
        });
    }

    private void updateViews(View root) {
        RecyclerView recyclerView = root.findViewById(R.id.my_story_recyclerView);
        TextView userHasNoStory = root.findViewById(R.id.error_message);

        MyStoriesAdapter adapter = new MyStoriesAdapter(storyList, context);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        progressBar.setVisibility(View.GONE);
        if (storyList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            userHasNoStory.setVisibility(View.VISIBLE);
        } else {
            userHasNoStory.setVisibility(View.GONE);
        }
    }

}
