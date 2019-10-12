package com.project.android_kidstories.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.android_kidstories.R;
import com.project.android_kidstories.SingleStoryActivity;
import com.project.android_kidstories.adapters.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class NewStoriesFragment extends Fragment implements RecyclerAdapter.StoryListener {


    RecyclerView recyclerView;
    GridLayoutManager layoutManager;

    List<String> storyName;
    List<Integer> storyPic;


    public static NewStoriesFragment newInstance(){return new NewStoriesFragment();}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_newstories,container,false);
        ButterKnife.bind(this,v);


        recyclerView = v.findViewById(R.id.recyclerView);
        layoutManager = new GridLayoutManager(getContext(), 2);
        storyName = new ArrayList<>();
        storyPic = new ArrayList<>();

        //Place holder data for recyclerView on mainAct

        storyPic.add(R.drawable.amex);
        storyPic.add(R.drawable.amex);
        storyPic.add(R.drawable.amex);
        storyPic.add(R.drawable.amex);
        storyPic.add(R.drawable.amex);
        storyPic.add(R.drawable.amex);
        storyPic.add(R.drawable.amex);
        storyPic.add(R.drawable.amex);


        storyName.add("Story of a Spartan!");
        storyName.add("Good and Love...");
        storyName.add("Random Name");
        storyName.add("Random Name");
        storyName.add("Random Name");
        storyName.add("Random Name");
        storyName.add("Random Name");
        storyName.add("Random Name");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerAdapter myAdapter = new RecyclerAdapter(getContext(), storyName, storyPic);
        myAdapter.setStoryListener(this);
        recyclerView.setAdapter(myAdapter);


        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onStoryClick() {
        startActivity(new Intent(requireActivity(), SingleStoryActivity.class));

    }
}
