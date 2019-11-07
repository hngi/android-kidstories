package com.project.android_kidstories.ui.home.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.project.android_kidstories.Api.Api;
import com.project.android_kidstories.Api.Responses.bookmark.BookmarkResponse;
import com.project.android_kidstories.Api.Responses.bookmark.UserBookmarkResponse;
import com.project.android_kidstories.Api.Responses.story.StoryAllResponse;
import com.project.android_kidstories.DataStore.Repository;
import com.project.android_kidstories.Model.Story;
import com.project.android_kidstories.R;
import com.project.android_kidstories.Utils.Common;
import com.project.android_kidstories.adapters.RecyclerStoriesAdapter;
import com.project.android_kidstories.sharePref.SharePref;
import com.project.android_kidstories.ui.home.BaseFragment;
import com.project.android_kidstories.ui.home.StoryAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class NewStoriesFragment extends BaseFragment implements StoryAdapter.OnStoryClickListener, View.OnClickListener,
//        RecyclerStoriesAdapter.OnBookmarked,
        RecyclerStoriesAdapter.StorySearch {

    private static final String TAG = "kidstories";
    private RecyclerView recyclerView;
    private RecyclerStoriesAdapter adapter;
    private ProgressBar progressBar;
    private Repository repository;
    private StoryViewModel viewModel;
    private RecyclerStoriesAdapter storyAdapter;
    private String token;
    public static RecyclerStoriesAdapter.StorySearch storySearchListener;
    SwipeRefreshLayout refreshLayout;


    public static NewStoriesFragment newInstance() {
        return new NewStoriesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_newstories, container, false);
        token = "Bearer " + new SharePref(getContext()).getMyToken();
        RecyclerStoriesAdapter.token = token;
        repository = Repository.getInstance(getActivity().getApplication());

        progressBar = v.findViewById(R.id.new_stories_bar);
        progressBar.setVisibility(View.GONE);
        recyclerView = v.findViewById(R.id.recyclerView);
        refreshLayout = v.findViewById(R.id.swipe_refresh);
        refreshLayout.setRefreshing(true);

        viewModel = ViewModelProviders.of(this.getActivity()).get(StoryViewModel.class);
        viewModel.token = token;

        fetchStories();



        return v;
    }

    private void fetchStories(){
        Observer<StoryAllResponse> observer = storyAllResponse -> {

//            Log.d("XXXX  stories", storyAllResponse.getData().toString());

            storyAdapter = new RecyclerStoriesAdapter(getContext(), storyAllResponse, repository);
            int spanCount;
            try {
                spanCount = getContext().getResources().getInteger(R.integer.home_fragment_gridspan);
            } catch (NullPointerException e) {
                spanCount = 1;
            }
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(storyAdapter);
            refreshLayout.setRefreshing(false);
        };
        viewModel.fetchStories("1").observe(this, observer);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        refreshLayout.setOnRefreshListener(() -> fetchStories());
    }

    @Override
    public void onStoryClick(Story story) {
        showToast(story.getTitle());
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        storySearchListener = this;
    }

    @Override
    public void onStorySearched(String query) {
        storyAdapter.getFilter().filter(query);
    }
}
