package com.project.android_kidstories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.project.android_kidstories.adapters.SectionsPageAdapter;
import com.project.android_kidstories.fragments.CategoriesFragment;
import com.project.android_kidstories.fragments.NewStoriesFragment;
import com.project.android_kidstories.fragments.PopularStoriesFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.container)
    ViewPager mViewPager;

    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setupViewPager(mViewPager);
        mTabLayout.setupWithViewPager(mViewPager);

    }


    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(NewStoriesFragment.newInstance(),"New Stories");
        adapter.addFragment(PopularStoriesFragment.newInstance(),"Popular Stories");
        adapter.addFragment(CategoriesFragment.newInstance(),"Categories");
        viewPager.setAdapter(adapter);
    }
}
