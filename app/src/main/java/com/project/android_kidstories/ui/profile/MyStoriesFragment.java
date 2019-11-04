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
import com.project.android_kidstories.Api.Responses.story.StoryAllResponse;
import com.project.android_kidstories.DataStore.Repository;
import com.project.android_kidstories.R;
import com.project.android_kidstories.data.model.Story;
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

    private ProgressBar progressBar;

    public static MyStoriesFragment getInstance() {
        return new MyStoriesFragment();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_my_stories, container, false);
        progressBar = root.findViewById(R.id.loading_bar);

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

        Repository repository = new Repository(context);
        repository.getStoryApi().getAllStories().enqueue(new Callback<StoryAllResponse>() {
            @Override
            public void onResponse(Call<StoryAllResponse> call, Response<StoryAllResponse> response) {
                if (response.isSuccessful()) {
                    StoryAllResponse storyAllResponse = response.body();
                    if (storyAllResponse == null) {
                        // Nothing was received
                        showSnack(view, "No story received");
                        progressBar.setVisibility(View.GONE);
                        return;
                    }
                    List<Story> stories = storyAllResponse.getData();
                    for (int i = 0; i < stories.size(); i++) {
                        if (stories.get(i).getUserId() == userId) {
                            storyList.add(stories.get(i));
                        }
                    }

                    updateViews(view);
                }
            }

            @Override
            public void onFailure(Call<StoryAllResponse> call, Throwable t) {
                showSnack(view, "Can't retrieve your stories, check connectivity and try again");
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void updateViews(View root) {
        RecyclerView recyclerView = root.findViewById(R.id.my_story_recyclerView);
        TextView errorMessage = root.findViewById(R.id.error_message);

        MyStoriesAdapter adapter = new MyStoriesAdapter(storyList, context);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        progressBar.setVisibility(View.GONE);
        if (storyList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            errorMessage.setVisibility(View.VISIBLE);
        } else {
            errorMessage.setVisibility(View.GONE);
        }
    }

}
