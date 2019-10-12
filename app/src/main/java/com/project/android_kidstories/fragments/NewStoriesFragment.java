package com.project.android_kidstories.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.project.android_kidstories.R;

import butterknife.ButterKnife;

public class NewStoriesFragment extends Fragment {

    public static NewStoriesFragment newInstance(){return new NewStoriesFragment();}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_newstories,container,false);
        ButterKnife.bind(this,v);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }
}
