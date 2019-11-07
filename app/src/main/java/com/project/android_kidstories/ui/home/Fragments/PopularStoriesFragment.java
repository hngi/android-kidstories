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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.ButterKnife;

import com.pixplicity.easyprefs.library.Prefs;
import com.project.android_kidstories.Api.Api;
import com.project.android_kidstories.Api.Responses.bookmark.BookmarkResponse;
import com.project.android_kidstories.Api.Responses.bookmark.UserBookmarkResponse;
import com.project.android_kidstories.Api.Responses.story.StoryAllResponse;
import com.project.android_kidstories.Api.RetrofitClient;
import com.project.android_kidstories.DataStore.Repository;
import com.project.android_kidstories.Model.Story;
import com.project.android_kidstories.R;
import com.project.android_kidstories.Utils.Common;
import com.project.android_kidstories.adapters.RecyclerStoriesAdapter;
import com.project.android_kidstories.sharePref.SharePref;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PopularStoriesFragment extends Fragment implements
        RecyclerStoriesAdapter.StorySearch {
    private RecyclerStoriesAdapter adapter;
    private ProgressBar popular_bar;
    RecyclerView recyclerView;
    Repository repository;
    private String token;
    StoryViewModel viewModel;
    public static RecyclerStoriesAdapter.StorySearch storySearchListener;
    SwipeRefreshLayout refreshLayout;

    public static PopularStoriesFragment newInstance() {
        return new PopularStoriesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_popularstories, container, false);
        ButterKnife.bind(this, v);
        repository = Repository.getInstance(getActivity().getApplication());
        popular_bar = v.findViewById(R.id.popular_stories_bar);
        token = "Bearer " + new SharePref(getContext()).getMyToken();
        popular_bar.setVisibility(View.GONE);
        recyclerView = v.findViewById(R.id.recyclerView);
        refreshLayout = v.findViewById(R.id.swipe_refresh2);
        refreshLayout.setRefreshing(true);
        viewModel = ViewModelProviders.of(this.getActivity()).get(StoryViewModel.class);
        viewModel.token = token;
        fetchStories();

        return v;
    }

    private void fetchStories() {

        Observer<StoryAllResponse> observer = storyAllResponse -> {
            adapter = new RecyclerStoriesAdapter(getContext(), sortList(storyAllResponse),
//                    PopularStoriesFragment.this,
                    repository);
            int spanCount;
            try {
                spanCount = getContext().getResources().getInteger(R.integer.home_fragment_gridspan);
            } catch (NullPointerException e) {
                spanCount = 1;
            }
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
            refreshLayout.setRefreshing(false);
        };
        viewModel.fetchStories().observe(this, observer);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        refreshLayout.setOnRefreshListener(() -> {
            fetchStories();
        });
    }

    private StoryAllResponse sortList(StoryAllResponse allResponse) {
        List<Story> allStories = allResponse.getData();
        StoryComparitor storyComparitor = new StoryComparitor();
        Collections.sort(allStories, storyComparitor);

        StoryAllResponse response = new StoryAllResponse();
        response.setData(allStories);
        return response;

    }

//    @Override

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        storySearchListener = this;
    }

    @Override
    public void onStorySearched(String query) {
        adapter.getFilter().filter(query);
    }

    public class StoryComparitor implements Comparator<Story> {

        @Override
        public int compare(Story story1, Story story2) {
            int likes_dislikes_1 = story1.getDislikesCount() + story1.getLikesCount();
            int likes_dislikes_2 = story2.getDislikesCount() + story2.getLikesCount();

            if (likes_dislikes_1 > likes_dislikes_2)
                return -1;
            else if (likes_dislikes_1 > likes_dislikes_2)
                return +1;
            else
                return 0;
        }
    }

}
