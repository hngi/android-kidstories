package com.project.android_kidstories.Views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.project.android_kidstories.R;


public class OnboardingPageAdapter extends PagerAdapter {

        private Context mContext;
        private List<OnboardingItem> onboardingItemList;

        public OnboardingPageAdapter(Context mContext, List<OnboardingItem> onboardingItemList) {
            this.mContext = mContext;
            this.onboardingItemList = onboardingItemList;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layoutScreen = inflater.inflate(R.layout.layout_screen, null);

            ImageView imgSlide = layoutScreen.findViewById(R.id.intro_img);
            TextView title = layoutScreen.findViewById(R.id.intro_title);
            TextView description = layoutScreen.findViewById(R.id.into_description);

            title.setText(onboardingItemList.get(position).getTitle());
            description.setText(onboardingItemList.get(position).getDescription());
            imgSlide.setImageResource(onboardingItemList.get(position).getImageResourceId());

            container.addView(layoutScreen);

            return layoutScreen;
        }

        @Override
        public int getCount() {
            return onboardingItemList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

            container.removeView((View) object);
        }
    }



