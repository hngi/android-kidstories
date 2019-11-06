package com.project.android_kidstories.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.project.android_kidstories.R;
import com.project.android_kidstories.adapters.RecyclerStoriesAdapter;
import com.project.android_kidstories.data.model.Story;
import com.project.android_kidstories.data.source.remote.api.Api;
import com.project.android_kidstories.data.source.remote.api.RetrofitClient;
import com.project.android_kidstories.data.source.remote.response_models.bookmark.UserBookmarkResponse;
import com.project.android_kidstories.ui.base.BaseFragment;
import com.project.android_kidstories.ui.profile.adapters.BookmarksAdapter;
import com.project.android_kidstories.ui.story_viewing.SingleStoryActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;

public class BookmarksFragment extends BaseFragment implements BookmarksAdapter.OnBookmarkClickListener {

    @BindView(R.id.bookmark_bar)
    ProgressBar progressBar;

    @BindView(R.id.bookmark_recycler)
    RecyclerView recyclerView;

    private View errorView;

    private BookmarksAdapter adapter;
    private ArrayList<Story> stories = new ArrayList<>();
    private String token;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        ButterKnife.bind(this, root);

        errorView = root.findViewById(R.id.error_msg);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);

        progressBar.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        refreshData();
    }

    private void refreshData() {
        /*Create handle for the RetrofitInstance interface*/
        Api service = RetrofitClient.getInstance().create(Api.class);
        token = "Bearer " + getSharePref().getUserToken();
        RecyclerStoriesAdapter.token = token;

        Call<UserBookmarkResponse> bookmarks = service.getUserBookmarks(token);

        bookmarks.enqueue(new Callback<UserBookmarkResponse>() {
            @Override
            public void onResponse(Call<UserBookmarkResponse> call, Response<UserBookmarkResponse> response) {
                stories.clear();
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    List<Story> data = response.body().getData();
                    stories.addAll(data);
                    adapter = new BookmarksAdapter(stories, BookmarksFragment.this, requireContext());
                    recyclerView.setAdapter(adapter);

                } else {
                    errorView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<UserBookmarkResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                errorView.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void onStoryClick(int storyId) {
        Intent intent = new Intent(getContext(), SingleStoryActivity.class);
        intent.putExtra(SingleStoryActivity.STORY_ID_KEY, storyId);
        requireContext().startActivity(intent);
    }

}
