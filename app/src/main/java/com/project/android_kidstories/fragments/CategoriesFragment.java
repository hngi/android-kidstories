package com.project.android_kidstories.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.android_kidstories.Model.RecyclerAdapter;
import com.project.android_kidstories.R;

import java.util.ArrayList;
import java.util.Arrays;


public class CategoriesFragment extends Fragment {

    ArrayList<String> authors = new ArrayList<>(Arrays.asList("Cow", "Goat", "Snail", "Assorted","Chicken", "Turkey","Crab","Pomo"));
    ArrayList<Integer> images = new ArrayList<>(Arrays.asList(R.drawable.chapman, R.drawable.chapman, R.drawable.chapman, R.drawable.chapman,R.drawable.chapman, R.drawable.chapman,R.drawable.chapman,R.drawable.chapman));

    RecyclerView recyclerView;

    public static CategoriesFragment newInstance(){
        return new CategoriesFragment();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_categories,container,false);
        recyclerView = v.findViewById(R.id.category_recycler);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(getContext(), images, authors);
        recyclerView.setAdapter(recyclerAdapter);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

    }

}
