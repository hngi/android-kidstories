package com.project.android_kidstories;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class OnBoardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
    }
    // TODO: Remove these codes
/*    public void openDonateActivity(View view) {
        startActivity(new Intent(OnBoardingActivity.this, DonateActivity.class));
    }

    public void openAboutActivity(View view) {
        startActivity(new Intent(OnBoardingActivity.this, StoryListingActivity.class));
    }

    public void openLoginActivity(View view) {
        startActivity(new Intent(OnBoardingActivity.this, LoginActivity.class));
    }

    public void openRegisterActivity(View view) {
        startActivity(new Intent(OnBoardingActivity.this, RegisterActivity.class));
    }

    public void openMainActivity(View view) {
        startActivity(new Intent(OnBoardingActivity.this, MainActivity.class));
    }

    public void openBookDetailActivity(View view) {
        startActivity(new Intent(OnBoardingActivity.this, StoryDetailActivity.class));
    }

    public void openAddStoryActivity(View view) {
        startActivity(new Intent(OnBoardingActivity.this, AddStoryActivity.class));
    }

    public void openEditProfileActivity(View view) {
        startActivity(new Intent(OnBoardingActivity.this, EditProfile.class));
    }

    public void openBookmarkActivity(View view) {
        startActivity(new Intent(OnBoardingActivity.this, BookMark.class));
    }*/
}
