package com.project.android_kidstories.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.android_kidstories.R;
import com.project.android_kidstories.adapters.MyStoriesAdapter;
import com.project.android_kidstories.viewModel.FragmentsSharedViewModel;

public class MyStoriesFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private MyStoriesAdapter adapter;
    private FragmentsSharedViewModel viewModel;

    public MyStoriesFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_stories, container, false);
        initView();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(getActivity()).get(FragmentsSharedViewModel.class);
        adapter = new MyStoriesAdapter(viewModel.currentUsersStories);
    }

    public void initView(){

        recyclerView = view.findViewById(R.id.my_story_recyclerView);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setVisibility(View.GONE);

    }


}
