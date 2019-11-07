package com.project.android_kidstories.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.android_kidstories.Api.Responses.story.StoryAllResponse;
import com.project.android_kidstories.DataStore.Repository;
import com.project.android_kidstories.Model.Story;
import com.project.android_kidstories.R;
import com.project.android_kidstories.SingleStoryActivity;
import com.project.android_kidstories.Views.main.MainActivity;
import com.project.android_kidstories.adapters.MyStoriesAdapter;
import com.project.android_kidstories.sharePref.SharePref;
import com.project.android_kidstories.viewModel.FragmentsSharedViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyStoriesFragment extends Fragment {

    private ViewGroup view;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private MyStoriesAdapter adapter;
    private FragmentsSharedViewModel viewModel;
    private TextView errorMessage;
    private ProgressBar loadingBar;
    private Repository repository;
    private SharePref sharePref;
    private List<Story> storyList = new ArrayList<>();

    public MyStoriesFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = (ViewGroup) inflater.inflate(R.layout.fragment_my_stories, container, false);
        recyclerView = view.findViewById(R.id.my_story_recyclerView);
        errorMessage = view.findViewById(R.id.error_message);
        loadingBar = view.findViewById(R.id.loading_bar);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(getActivity()).get(FragmentsSharedViewModel.class);
        sharePref = SharePref.getINSTANCE(getActivity().getApplicationContext());



        repository = new Repository(getActivity().getApplicationContext());
        fetchStories("1");

    }

    void fetchStories(String page){
        repository.getStoryApi().getAllStories(page).enqueue(new Callback<StoryAllResponse>() {
            @Override
            public void onResponse(Call<StoryAllResponse> call, Response<StoryAllResponse> response) {
                if(response.isSuccessful()){
                    for(int i = 0; i < response.body().getStories().size(); i++){
                        if(response.body().getStories().get(i).getUserId() == sharePref.getUserId()){
                            storyList.add(response.body().getStories().get(i));
                        }
                    }
                    //errorMessage.setText(storyList.toString());
                    initView();

                }
            }

            @Override
            public void onFailure(Call<StoryAllResponse> call, Throwable t) {

                errorMessage.setVisibility(View.VISIBLE);
                errorMessage.setText("Check Connectivity and try again");
            }
        });
    }

    public void initView(){

        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        adapter = new MyStoriesAdapter(storyList, getActivity().getApplicationContext(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStoryClick(storyList.get(recyclerView.getChildLayoutPosition(view)).getId());
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        loadingBar.setVisibility(View.GONE);
        if(storyList.isEmpty()){
            recyclerView.setVisibility(View.GONE);
            errorMessage.setVisibility(View.VISIBLE);
        }
        else{
           errorMessage.setVisibility(View.GONE);
        }

    }

    public void onStoryClick(int storyId) {
        Intent intent = new Intent(getContext(), SingleStoryActivity.class);
        intent.putExtra("story_id", storyId);
        getContext().startActivity(intent);
    }


}
