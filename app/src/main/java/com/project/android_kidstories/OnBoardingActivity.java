package com.project.android_kidstories;

import androidx.appcompat.app.AppCompatActivity;


import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class OnBoardingActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private OnboardingPageAdapter onboardingPageAdapter;
    private List<OnboardingItem> mList;
    private TabLayout tabLayout;
    private Button btnNext;
    private int position = 0;
    private Button btnGetStarted;
    private Animation btnAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_on_boarding);

        //When this  activity is about to get launched, we need to check if that is the apps initial launching
        if (restorePrefData()){
            Intent mainActivity = new Intent(OnBoardingActivity.this, MainActivity.class);
            startActivity(mainActivity);
            finish();
        }

        setContentView(R.layout.activity_on_boarding);


        //ini views
        tabLayout = findViewById(R.id.tab_indicator);
        btnNext = findViewById(R.id.button_next);
        btnGetStarted = findViewById(R.id.btn_start_shopping);
        btnAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_animation);


        mList = new ArrayList<>();
        mList.add(new OnboardingItem("Read free bedtime stories , fairytales and poems for kids"," " , R.drawable.book_1));
        mList.add(new OnboardingItem("Create new stories that are available offline", "", R.drawable.book_2));

        //Set up view pager
        viewPager = findViewById(R.id.onbaord_viewpager);
        onboardingPageAdapter = new OnboardingPageAdapter(this, mList);
        viewPager.setAdapter(onboardingPageAdapter);

        //Set up tabLayout with viewPager
        tabLayout.setupWithViewPager(viewPager);



        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position = viewPager.getCurrentItem();
                if (position < mList.size()){
                    position++;
                    viewPager.setCurrentItem(position);
                }

                if (position == mList.size() - 1){
                    // On getting to the last page of the pagerAdapter
                    loadScreen();
                }
            }
        });

        //tabLayout change listener
        tabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == mList.size() - 1){
                    loadScreen();
                } else {
                    btnNext.setVisibility(View.VISIBLE);
                    tabLayout.setVisibility(View.VISIBLE);
                    btnGetStarted.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getPosition() == mList.size() - 1){
                    loadScreen();
                } else {
                    btnNext.setVisibility(View.VISIBLE);
                    tabLayout.setVisibility(View.VISIBLE);
                    btnGetStarted.setVisibility(View.INVISIBLE);
                }
            }
        });

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(OnBoardingActivity.this, MainActivity.class));
                //Save value to storage to indicate that user has already seen the onboarding in order not to repeat
                //use sharepref
                savePrefData();
                finish();
            }
        });
    }

    private boolean restorePrefData() {
        boolean isOnboardingDone;
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("onboardingPref", MODE_PRIVATE);
        isOnboardingDone = sharedPreferences.getBoolean("isOnboarding", false);
        return isOnboardingDone;
    }

    private void savePrefData() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("onboardingPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isOnboarding", true);
        editor.apply();
    }


    private void loadScreen() {
        btnGetStarted.setAnimation(btnAnimation);
        btnNext.setVisibility(View.INVISIBLE);
        tabLayout.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
    }
}
