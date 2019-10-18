package com.project.android_kidstories.ui.home.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.project.android_kidstories.ui.home.Fragments.CategoriesFragment;
import com.project.android_kidstories.ui.home.Fragments.NewStoriesFragment;
import com.project.android_kidstories.ui.home.Fragments.PopularStoriesFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter
{


    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment activeFragment=null;
        switch(position){
            case 0: activeFragment=new NewStoriesFragment();
                break;
            case 1: activeFragment=new PopularStoriesFragment();
                break;
            case 2: activeFragment=new CategoriesFragment();
                break;
        }
        return activeFragment;
    }


    @Override
    public int getCount()
    {
        // TODO: Implement this method
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        String title="";
        switch(position){
            case 0:title="New Stories";
                break;
            case 1:title="Popular Stories";
                break;
            case 2:title="Categories";
                break;

        }
        return title;
    }





}