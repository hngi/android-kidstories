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

    private BookmarksAdapter adapter;
    private ArrayList<Story> stories = new ArrayList<>();
    String token;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_bookmarks, container, false);
        ButterKnife.bind(this, root);

        recyclerView.setAdapter(new BookmarksAdapter(stories, BookmarksFragment.this, requireContext()));

        progressBar.setVisibility(View.VISIBLE);

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
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    recyclerView.setLayoutManager(layoutManager);

                    List<Story> data = response.body().getData();
                    for (Story s : data) {
                        stories.add(s);
                    }
                    adapter = new BookmarksAdapter(stories,BookmarksFragment.this, getContext());
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                } else {
                    showSnack(root, "Something went wrong...Please try later!");
                }
            }

            @Override
            public void onFailure(Call<UserBookmarkResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                showSnack(root, "Can't retrieve your stories, check connectivity and try again");
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }


    @Override
    public void onStoryClick(int storyId) {
        Intent intent = new Intent(getContext(), SingleStoryActivity.class);
        intent.putExtra("story_id", storyId);
        getContext().startActivity(intent);
    }

}
