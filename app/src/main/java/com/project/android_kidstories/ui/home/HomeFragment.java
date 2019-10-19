package com.project.android_kidstories.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.project.android_kidstories.AddStoryActivity;
import com.project.android_kidstories.R;

import com.project.android_kidstories.ui.home.Adapters.SectionsPageAdapter;
import com.project.android_kidstories.ui.home.Fragments.CategoriesFragment;
import com.project.android_kidstories.ui.home.Fragments.NewStoriesFragment;
import com.project.android_kidstories.ui.home.Fragments.PopularStoriesFragment;

public class HomeFragment extends Fragment {
    ViewPager viewPager;
    TabLayout tabLayout;
    AppBarLayout appBarLayout;

    private com.project.android_kidstories.ui.home.HomeViewModel homeViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(com.project.android_kidstories.ui.home.HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        tabLayout = root.findViewById(R.id.home_frag_tablayout);
        viewPager = root.findViewById(R.id.home_frag_container);
        appBarLayout = root.findViewById(R.id.home_frag_appbar);;

        FloatingActionButton floatingActionButton = root.findViewById(R.id.home_frag_calculate_fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeFragment.this.getActivity(), AddStoryActivity.class);
                startActivity(intent);
            }
        });


        SectionsPageAdapter adapter = new SectionsPageAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(NewStoriesFragment.newInstance(), "New Stories");
        adapter.addFragment(PopularStoriesFragment.newInstance(), "Popular Stories");
        adapter.addFragment(CategoriesFragment.newInstance(), "Categories");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


        return root;
    }
    @Override
    public void onDestroyView () {
        super.onDestroyView ();super . onDestroyView ();
        appBarLayout.removeView(tabLayout);
    }
}