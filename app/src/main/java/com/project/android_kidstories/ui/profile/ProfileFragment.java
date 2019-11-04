package com.project.android_kidstories.ui.profile;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.project.android_kidstories.R;
import com.project.android_kidstories.data.source.local.preferences.SharePref;
import com.project.android_kidstories.db.Helper.BedTimeDbHelper;
import com.project.android_kidstories.ui.MainActivity;
import com.project.android_kidstories.ui.base.BaseFragment;
import com.project.android_kidstories.ui.profile.adapters.ProfilePagerAdapter;
import com.project.android_kidstories.ui.staging.ImageStagingActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import org.jetbrains.annotations.NotNull;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends BaseFragment {

    private CircleImageView imageView;
    private SharePref sharePref;

    private BedTimeDbHelper helper;

    private static final int REQUEST_WRITE_PERMISSION = 786;
    private static int REQUEST_LOAD_IMAGE = 1;
    private static int REQUEST_CROP_IMAGE = 2;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.profile_fragment, container, false);

        helper = new BedTimeDbHelper(getContext());
        sharePref = getSharePref();

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
                .setOnClickListener(v -> choosePictureWithPermission());
    }

    private void choosePictureWithPermission() {
        //request for permission if not granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
        } else {
            Intent images = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(images, REQUEST_LOAD_IMAGE);
        }
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
        String fullname = sharePref.getUserFullname();
        String email = sharePref.getUserEmail();

        TextView userName = root.findViewById(R.id.profile_name);
        TextView userEmail = root.findViewById(R.id.profile_email);

        userEmail.setText(email);
        userName.setText(fullname);

        setProfileImage();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent images = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(images, REQUEST_LOAD_IMAGE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;

        if (requestCode == REQUEST_LOAD_IMAGE && data != null) {
            try {
                Uri selected_image = data.getData();
                if (selected_image == null) throw new NullPointerException();
                String uriString = selected_image.toString();

                // Start the staging activity
                ImageStagingActivity.startForResult(this, uriString, REQUEST_CROP_IMAGE);

            } catch (Exception e) {
                showToast("No picture was selected");
            }
        }
        if (requestCode == REQUEST_CROP_IMAGE) {
            // Image crop was successful
            setProfileImage();
            ((MainActivity) requireActivity()).updateProfileImage();
        }
    }

}
