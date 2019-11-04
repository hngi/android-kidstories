package com.project.android_kidstories.ui.profile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.project.android_kidstories.R;
import com.project.android_kidstories.adapters.ProfilePagerAdapter;
import com.project.android_kidstories.db.Helper.BedTimeDbHelper;
import com.project.android_kidstories.sharePref.SharePref;
import com.project.android_kidstories.ui.profile.editprofile.EditProfileFragment;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    private CircleImageView imageView;
    private SharePref sharePref;

    private BedTimeDbHelper helper;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.profile_fragment, container, false);

        helper = new BedTimeDbHelper(getContext());
        sharePref = new SharePref(requireContext());

        imageView = root.findViewById(R.id.profile);

        initViews(root);

        return root;
    }

    private void initViews(View root) {
        // Setup ViewPager
        ProfilePagerAdapter pagerAdapter = new ProfilePagerAdapter(getChildFragmentManager());
        // add fragments to adapter
        pagerAdapter.addFragment(MyStoriesFragment.getInstance());
        pagerAdapter.addFragment(new BookmarksFragment());

        // initiate viewPager
        ViewPager viewPager = root.findViewById(R.id.viewPager);
        viewPager.setAdapter(pagerAdapter);

        // initiate tabLayout
        TabLayout tabLayout = root.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("My Stories");
        tabLayout.getTabAt(1).setText("Bookmarked");
        tabLayout.setTabTextColors(R.color.grey, R.color.black);

        displayProfile(root);

        root.findViewById(R.id.img_edit_profile)
                .setOnClickListener(v -> editProfile());
    }

    private void editProfile() {
        requireFragmentManager().beginTransaction()
                .replace(R.id.main_fragment_container, EditProfileFragment.getInstance())
                .commit();
    }

    private void setProfileImage() {
        byte[] imageArray = helper.getUserImage();
        if (imageArray != null) {
            Bitmap image = BitmapFactory.decodeByteArray(imageArray, 0, imageArray.length);
            imageView.setImageBitmap(image);
        } else {
            imageView.setImageDrawable(requireActivity().getDrawable(R.drawable.ic_account_circle_black_150dp));
        }
    }

    private void displayProfile(View root) {
        String firstname = sharePref.getUserFirstname();
        String lastname = sharePref.getUserLastname();
        String email = sharePref.getUserEmail();

        String name = firstname + " " + lastname;

        TextView userName = root.findViewById(R.id.profile_name);
        TextView userEmail = root.findViewById(R.id.profile_email);

        userEmail.setText(email);
        userName.setText(name);

        setProfileImage();
    }

}
