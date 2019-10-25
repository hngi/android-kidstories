package com.project.android_kidstories.ui.home.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pixplicity.easyprefs.library.Prefs;
import com.project.android_kidstories.Api.Api;
import com.project.android_kidstories.Api.Responses.bookmark.BookmarkResponse;
import com.project.android_kidstories.Api.Responses.bookmark.UserBookmarkResponse;
import com.project.android_kidstories.Api.Responses.story.StoryAllResponse;
import com.project.android_kidstories.Api.RetrofitClient;
import com.project.android_kidstories.DataStore.Repository;
import com.project.android_kidstories.Model.Story;
import com.project.android_kidstories.R;
import com.project.android_kidstories.adapters.RecyclerStoriesAdapter;
import com.project.android_kidstories.sharePref.SharePref;
import com.project.android_kidstories.ui.home.BaseFragment;
import com.project.android_kidstories.ui.home.StoryAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class NewStoriesFragment extends BaseFragment implements StoryAdapter.OnStoryClickListener, View.OnClickListener, RecyclerStoriesAdapter.OnBookmarked {

    private static final String TAG = "kidstories";
    private RecyclerView recyclerView;
    private RecyclerStoriesAdapter adapter;
    private ProgressBar progressBar;
    private Repository repository;
    private Api service;
    private boolean isAddSuccessful, initBookmark;
    //    private StoryAdapter storyAdapter;
    private RecyclerStoriesAdapter storyAdapter;
    private String token;


    public static NewStoriesFragment newInstance() {
        return new NewStoriesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_newstories, container, false);
        token = "Bearer " + new SharePref(getContext()).getMyToken();
        progressBar = v.findViewById(R.id.new_stories_bar);

        progressBar.setVisibility(View.VISIBLE);

        /*Create handle for the RetrofitInstance interface*/
        service = RetrofitClient.getInstance().create(Api.class);
        Call<StoryAllResponse> stories = service.getAllStories();

        stories.enqueue(new Callback<StoryAllResponse>() {
            @Override
            public void onResponse(Call<StoryAllResponse> call, Response<StoryAllResponse> response) {
                //  generateCategoryList(response.body(),v);
                progressBar.setVisibility(View.GONE);
                recyclerView = v.findViewById(R.id.recyclerView);

                if (response.isSuccessful()) {
                    storyAdapter = new RecyclerStoriesAdapter(getContext(), response.body(), NewStoriesFragment.this);
                    int spanCount;
                    try {
                        spanCount = getContext().getResources().getInteger(R.integer.home_fragment_gridspan);
                    } catch (NullPointerException e) {
                        spanCount = 1;
                    }
                    GridLayoutManager layoutManager = new GridLayoutManager(getContext(), spanCount);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(storyAdapter);
                } else {
                    Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StoryAllResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

       /*
        recyclerView = v.findViewById(R.id.new_story_frag_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));*/

        /*
        ApiViewmodel apiViewmodel = ViewModelProviders.of(getActivity()).get(ApiViewmodel.class);
        repository = apiViewmodel.getRepository();
        storyAdapter = new StoryAdapter(apiViewmodel);
        storyAdapter.setOnStoryClickListener(this);
        recyclerView.setAdapter(storyAdapter);


        fetchStories();*/

    /*

    private void fetchStories() {
        if (!Common.checkNetwork(getActivity())) {
            showToast("You do not have network connection");
        }
        repository.getApi().getAllStories().enqueue(new Callback<StoryAllResponse>() {
            @Override
            public void onResponse(Call<StoryAllResponse> call, Response<StoryAllResponse> response) {
                if (response.isSuccessful()) {
                    List<Story> storyList = response.body().getData();
                    storyAdapter.summitStories(storyList);
                    Log.d(TAG, "getAllStories Successful: Stories " + storyList.size());
                }
            }

            @Override
            public void onFailure(Call<StoryAllResponse> call, Throwable t) {
                Log.w(TAG, "onFailure: " + t.getMessage());
            }
        });
    }*/

    @Override
    public void onStoryClick(Story story) {
        showToast(story.getTitle());
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onBookmarkAdded(int storyId) {

        Call<BookmarkResponse> addBookmark = service.bookmarkStory(token, storyId);
        addBookmark.enqueue(new Callback<BookmarkResponse>() {
            @Override
            public void onResponse(Call<BookmarkResponse> call, Response<BookmarkResponse> response) {
                if (response.isSuccessful()) {
                    Prefs.putBoolean(String.valueOf(storyId),true);
                    Toast.makeText(getContext(), "Bookmark added", Toast.LENGTH_SHORT).show();
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
    public boolean isAlreadyBookmarked(int storyId, int pos) {

        Call<UserBookmarkResponse> bookmarks = service.getUserBookmarks(token);

        bookmarks.enqueue(new Callback<UserBookmarkResponse>() {
            @Override
            public void onResponse(Call<UserBookmarkResponse> call, Response<UserBookmarkResponse> response) {
                if (response.isSuccessful()) {
                    List<Story> data = response.body().getData();
                    for (Story s : data) {
                        if (s.getId() == storyId) {
                            Log.e("STORYID", storyId + "");
                            Prefs.putBoolean(String.valueOf(storyId),true);
                            initBookmark = true;
                        }
                    }
                } else {
                    Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserBookmarkResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
        Log.e("INITBOOKMARK", initBookmark + "");
        return initBookmark;
    }
}
