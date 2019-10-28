package com.project.android_kidstories.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.android_kidstories.Api.Responses.story.StoryAllResponse;
import com.project.android_kidstories.DataStore.Repository;
import com.project.android_kidstories.R;
import com.project.android_kidstories.Views.main.MainActivity;
import com.project.android_kidstories.adapters.MyStoriesAdapter;
import com.project.android_kidstories.viewModel.FragmentsSharedViewModel;

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
    private Repository repository;

    public MyStoriesFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = (ViewGroup) inflater.inflate(R.layout.fragment_my_stories, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(getActivity()).get(FragmentsSharedViewModel.class);

        repository = new Repository(getActivity().getApplicationContext());
        repository.getStoryApi().getAllStories().enqueue(new Callback<StoryAllResponse>() {
            @Override
            public void onResponse(Call<StoryAllResponse> call, Response<StoryAllResponse> response) {
                if(response.isSuccessful()){
                    adapter = new MyStoriesAdapter(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<StoryAllResponse> call, Throwable t) {

            }
        });
        //adapter = new MyStoriesAdapter(viewModel.currentUsersStories);
        initView();
    }

    public void initView(){

        recyclerView = view.findViewById(R.id.my_story_recyclerView);
        errorMessage = view.findViewById(R.id.error_message);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);

        //if(viewModel.currentUsersStories.isEmpty()){
          //  recyclerView.setVisibility(View.GONE);
        //}
        //else{
          // errorMessage.setVisibility(View.GONE);
        //}

    }


}
