package com.project.android_kidstories.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.project.android_kidstories.R;
import com.project.android_kidstories.fragments.CategoriesFragment;
import com.project.android_kidstories.fragments.PopularStoriesFragment;
import com.project.android_kidstories.ui.home.Adapters.SectionsPageAdapter;
import com.project.android_kidstories.ui.home.Fragments.NewStoriesFragment;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        TabLayout tabLayout = root.findViewById(R.id.home_frag_tablayout);
        ViewPager viewPager = root.findViewById(R.id.home_frag_container);


        SectionsPageAdapter adapter = new SectionsPageAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(NewStoriesFragment.newInstance(),"New Stories");
        adapter.addFragment(PopularStoriesFragment.newInstance(),"Popular Stories");
        adapter.addFragment(CategoriesFragment.newInstance(),"Categories");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);





        return root;
    }





}