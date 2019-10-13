package com.project.android_kidstories.Views.main.ui.group;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.project.android_kidstories.R;

public class CategoriesFragment extends Fragment {

    private com.project.android_kidstories.ui.group.CategoryViewModel categoryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        categoryViewModel = ViewModelProviders.of(this).get(com.project.android_kidstories.ui.group.CategoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_categories, container, false);


        return root;
    }
}