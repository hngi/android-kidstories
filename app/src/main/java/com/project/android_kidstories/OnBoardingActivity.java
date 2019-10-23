package com.project.android_kidstories;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.project.android_kidstories.sharePref.SharePref;

import java.util.ArrayList;
import java.util.List;

public class OnBoardingActivity extends AppCompatActivity {

    public static final String HAS_LAUNCHED_BEFORE = "HAS_LAUNCHED_BEFORE";

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

        setContentView(R.layout.activity_on_boarding);


        //ini views
        tabLayout = findViewById(R.id.tab_indicator);
        btnNext = findViewById(R.id.button_next);
        btnGetStarted = findViewById(R.id.btn_start_shopping);
        btnAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_animation);


        mList = new ArrayList<>();
        mList.add(new OnboardingItem("Read free bedtime stories , fairytales and poems for kids"," " , R.drawable.onboarding_ic));
        mList.add(new OnboardingItem("Create new stories that are available offline", "", R.drawable.onboarding_ic_two));

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
                //Save value to storage to indicate that user has already seen the onboarding in order not to repeat
                //use sharepref
                savePrefData();
                startActivity(new Intent(OnBoardingActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private boolean restorePrefData() {
        return SharePref.getINSTANCE(getApplicationContext()).getBool(HAS_LAUNCHED_BEFORE);
    }

    private void savePrefData() {
        SharePref sharePref = SharePref.getINSTANCE(getApplicationContext());
        sharePref.setBool(HAS_LAUNCHED_BEFORE, true);
    }


    private void loadScreen() {
        btnGetStarted.setAnimation(btnAnimation);
        btnNext.setVisibility(View.INVISIBLE);
        tabLayout.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
    }
}
