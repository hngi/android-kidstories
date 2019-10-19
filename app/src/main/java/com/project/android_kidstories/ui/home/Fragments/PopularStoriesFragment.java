package com.project.android_kidstories.ui.home.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.android_kidstories.Api.Api;
import com.project.android_kidstories.Api.Responses.story.StoryAllResponse;
import com.project.android_kidstories.Api.RetrofitClient;
import com.project.android_kidstories.Model.Story;
import com.project.android_kidstories.R;
import com.project.android_kidstories.adapters.RecyclerStoriesAdapter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopularStoriesFragment extends Fragment {
    private RecyclerStoriesAdapter adapter;
    ProgressDialog progressDoalog;
    RecyclerView recyclerView;

    public static PopularStoriesFragment newInstance(){return new PopularStoriesFragment();}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_popularstories,container,false);
        ButterKnife.bind(this,v);

        progressDoalog = new ProgressDialog(getActivity());
        progressDoalog.setMessage("Loading....");
        progressDoalog.show();

        /*Create handle for the RetrofitInstance interface*/
        Api service = RetrofitClient.getInstance().create(Api.class);
        Call<StoryAllResponse> stories = service.getAllStories();

        stories.enqueue(new Callback<StoryAllResponse>() {
            @Override
            public void onResponse(Call<StoryAllResponse> call, Response<StoryAllResponse> response) {
                //  generateCategoryList(response.body(),v);
                progressDoalog.dismiss();

                recyclerView = v.findViewById(R.id.recyclerView);
                if (response.isSuccessful()) {
                    adapter = new RecyclerStoriesAdapter(getContext(), sortList(response.body()));
                    GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 1);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                }else{
                    Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StoryAllResponse> call, Throwable t) {
                progressDoalog.dismiss();

                Toast.makeText(getContext(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    private StoryAllResponse sortList(StoryAllResponse allResponse){
        List<Story> allStories = allResponse.getData();


        StoryComparitor storyComparitor = new StoryComparitor();
        Collections.sort(allStories,storyComparitor);

        StoryAllResponse response = new StoryAllResponse();
        response.setData(allStories);
        return response;

    }

    public class StoryComparitor implements Comparator<Story> {

        @Override
        public int compare(Story story1, Story story2) {
            int likes_dislikes_1 = story1.getDislikesCount()+story1.getLikesCount();
            int likes_dislikes_2 = story2.getDislikesCount()+story2.getLikesCount();

            if(likes_dislikes_1 > likes_dislikes_2)
                return -1;
            else if(likes_dislikes_1 > likes_dislikes_2)
                return +1;
            else
                return 0;
        }
    }

}
