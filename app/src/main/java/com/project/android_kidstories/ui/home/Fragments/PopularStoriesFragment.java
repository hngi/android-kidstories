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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.ButterKnife;

import com.pixplicity.easyprefs.library.Prefs;
import com.project.android_kidstories.Api.Api;
import com.project.android_kidstories.Api.Responses.bookmark.BookmarkResponse;
import com.project.android_kidstories.Api.Responses.bookmark.UserBookmarkResponse;
import com.project.android_kidstories.Api.Responses.story.Data;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PopularStoriesFragment extends Fragment implements RecyclerStoriesAdapter.OnBookmarked, RecyclerStoriesAdapter.StorySearch {
    private RecyclerStoriesAdapter adapter;
    private ProgressBar popular_bar;
    RecyclerView recyclerView;
    Repository repository;
    private Api service;
    private boolean isAddSuccessful, initBookmark;
    private String token;
    public static RecyclerStoriesAdapter.StorySearch storySearchListener;
    SwipeRefreshLayout refreshLayout;
    private List<Story> storiesArray;

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
        popular_bar.setVisibility(View.VISIBLE);
        recyclerView = v.findViewById(R.id.recyclerView);
        refreshLayout = v.findViewById(R.id.swipe_refresh2);
        refreshLayout.setRefreshing(true);
        storiesArray = new ArrayList<>();

        fetchStories("1");

        return v;
    }

    private void fetchStories(String page){
        /*Create handle for the RetrofitInstance interface*/
        service = RetrofitClient.getInstance().create(Api.class);
        Call<StoryAllResponse> stories = service.getAllStoriesWithAuth(token,page);

        stories.enqueue(new Callback<StoryAllResponse>() {
            @Override
            public void onResponse(Call<StoryAllResponse> call, Response<StoryAllResponse> response) {
                //  generateCategoryList(response.body(),v);
                popular_bar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    List<Story> storiesList = sortList(response.body()).getStories();
                    storiesArray.addAll(storiesList);
                    adapter = new RecyclerStoriesAdapter(getContext(), storiesArray, PopularStoriesFragment.this,repository);

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
                } else {

                }
            }

            @Override
            public void onFailure(Call<StoryAllResponse> call, Throwable t) {
                popular_bar.setVisibility(View.INVISIBLE);

                //TODO
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        refreshLayout.setOnRefreshListener(() -> {
            fetchStories("1");
        });
    }

    private StoryAllResponse sortList(StoryAllResponse allResponse) {
        List<Story> allStories = allResponse.getStories();


        StoryComparator storyComparator = new StoryComparator();
        Collections.sort(allStories, storyComparator);

        StoryAllResponse response = new StoryAllResponse();
        List<Story> d ;
        d= allStories;
        response.setData(d);
        return response;

    }

    @Override
    public boolean onBookmarkAdded(int storyId) {
        Call<BookmarkResponse> addBookmark = service.bookmarkStory(token, storyId);
        addBookmark.enqueue(new Callback<BookmarkResponse>() {
            @Override
            public void onResponse(Call<BookmarkResponse> call, Response<BookmarkResponse> response) {
                if (response.isSuccessful()) {
                    Prefs.putBoolean(String.valueOf(storyId),true);
                    isAddSuccessful = true;
                } else {
                    isAddSuccessful = false;
                }
            }

            @Override
            public void onFailure(Call<BookmarkResponse> call, Throwable t) {
                isAddSuccessful = false;
            }
        });
        Log.e("ISADDSUCCESSFUL", isAddSuccessful + "");
        return isAddSuccessful;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        storySearchListener = this;


    }

    @Override
    public void onStorySearched(String query) {
        adapter.getFilter().filter(query);
    }

    public class StoryComparator implements Comparator<Story> {

        @Override
        public int compare(Story story1, Story story2) {


            int story1PriorityCount = story1.getDislikesCount() + story1.getLikesCount();
            int story2PriorityCount= story2.getDislikesCount() + story2.getLikesCount();

            return Integer.compare(story1PriorityCount, story2PriorityCount);
        }
    }

}
