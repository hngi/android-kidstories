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
import com.project.android_kidstories.adapters.SectionsPageAdapter;
import com.project.android_kidstories.fragments.CategoriesFragment;
import com.project.android_kidstories.fragments.NewStoriesFragment;
import com.project.android_kidstories.fragments.PopularStoriesFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment {
     @BindView(R.id.container)
     ViewPager mViewPager;

    @BindView(R.id.tabs1)
    TabLayout mTabLayout;

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mTabLayout = root.findViewById(R.id.tabs1);
        mViewPager = root.findViewById(R.id.container);

        ButterKnife.bind(getActivity());
        SectionsPageAdapter adapter = new SectionsPageAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(NewStoriesFragment.newInstance(),"New Stories");
        adapter.addFragment(PopularStoriesFragment.newInstance(),"Popular Stories");
        adapter.addFragment(CategoriesFragment.newInstance(),"Categories");
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        return root;
    }
}