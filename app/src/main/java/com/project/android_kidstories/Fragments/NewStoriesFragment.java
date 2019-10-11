package com.project.android_kidstories.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.project.android_kidstories.Adapters.RecyclerAdapter;
import com.project.android_kidstories.R;

import java.util.ArrayList;
import java.util.List;


public class NewStoriesFragment extends Fragment {

    RecyclerView recyclerView;
    GridLayoutManager layoutManager;

    List<String> storyName;
    List<Integer> storyPic;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_stories, container, false);


        recyclerView = view.findViewById(R.id.recyclerView);
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


        storyName.add("Random Name");
        storyName.add("Random Name");
        storyName.add("Random Name");
        storyName.add("Random Name");
        storyName.add("Random Name");
        storyName.add("Random Name");
        storyName.add("Random Name");
        storyName.add("Random Name");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerAdapter myAdapter = new RecyclerAdapter(getContext(), storyName, storyPic);
        recyclerView.setAdapter(myAdapter);

        return view;
    }


}
