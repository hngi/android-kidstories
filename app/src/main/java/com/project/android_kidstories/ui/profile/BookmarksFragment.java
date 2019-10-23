package com.project.android_kidstories.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pixplicity.easyprefs.library.Prefs;
import com.project.android_kidstories.Api.Api;
import com.project.android_kidstories.Api.Responses.bookmark.UserBookmarkResponse;
import com.project.android_kidstories.Api.RetrofitClient;
import com.project.android_kidstories.Model.Story;
import com.project.android_kidstories.R;
import com.project.android_kidstories.SingleStoryActivity;
import com.project.android_kidstories.adapters.BookmarksAdapter;
import com.project.android_kidstories.sharePref.SharePref;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookmarksFragment extends Fragment implements BookmarksAdapter.OnBookmarkClickListener {

    @BindView(R.id.bookmark_bar)
    ProgressBar progressBar;

    @BindView(R.id.bookmark_recycler)
    RecyclerView recyclerView;

    BookmarksAdapter adapter;
    ArrayList<Story> stories;
    String token;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bookmarks,container,false);
        ButterKnife.bind(this,v);
        stories = new ArrayList<>();
        recyclerView.setAdapter(null);
       /* recyclerView = v.findViewById(R.id.category_recycler);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);*/

        // Glide.with(this).load("http://i.imgur.com/DvpvklR.png").into(imageView);

       /* RecyclerStoriesAdapter recyclerAdapter = new RecyclerStoriesAdapter(getContext(), images, authors);
        recyclerView.setAdapter(recyclerAdapter);*/

        progressBar.setVisibility(View.VISIBLE);

        /*Create handle for the RetrofitInstance interface*/
        Api service = RetrofitClient.getInstance().create(Api.class);
        token = "Bearer "+ new SharePref(getContext()).getMyToken();
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
                    for(Story s: data){
                        stories.add(s);
                    }
                    adapter = new BookmarksAdapter(stories,BookmarksFragment.this);
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                }else {
                    Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserBookmarkResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
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
