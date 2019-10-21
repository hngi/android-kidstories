package com.project.android_kidstories.ui.profile;

import androidx.lifecycle.ViewModelProviders;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.project.android_kidstories.Api.Responses.BaseResponse;
import com.project.android_kidstories.Api.RetrofitClient;
import com.project.android_kidstories.DataStore.Repository;
import com.project.android_kidstories.Model.User;
import com.project.android_kidstories.R;
import com.project.android_kidstories.Utils.ImageConversion;
import com.project.android_kidstories.Views.main.MainActivity;
import com.project.android_kidstories.adapters.ProfilePagerAdapter;
import com.project.android_kidstories.db.Helper.AddUsers;
import com.project.android_kidstories.db.Helper.BedTimeDbHelper;
import com.project.android_kidstories.sharePref.SharePref;
import com.project.android_kidstories.ui.profile.BookmarksFragment;
import com.project.android_kidstories.ui.profile.MyStoriesFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    public ImageView imageView;
    BedTimeDbHelper helper;
    ImageConversion imageConversion;
    TextView userName, userEmail;
    String token;
    private Repository repository;

    private com.project.android_kidstories.ui.profile.ProfileViewModel mViewModel;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Enables options menu in this fragment
        setHasOptionsMenu(true);

        helper = new BedTimeDbHelper(getContext());
        imageConversion = new ImageConversion();

        int client_id = helper.getLastId(AddUsers.AddUsersColumn.TABLE_NAME);

        View root = inflater.inflate(R.layout.profile_fragment, container, false);

        imageView = root.findViewById(R.id.profile);
        userName = root.findViewById(R.id.profile_name);
        userEmail = root.findViewById(R.id.profile_email);

//        Get token from bundle
//        if (getArguments() != null) {
//            token = getArguments().getString("token");
//        }

//        Displays the user information
        displayProfile();

        // TODO: Causes the app to crash
        /*Bitmap image = imageConversion.convertByteArraytoBitMap(getImage(client_id));
        Bitmap resizedImage = imageConversion.fitBitMaptoImageView(image, 178, 178);
        imageView.setImageBitmap(resizedImage);*/

        // Setup ViewPager
        ProfilePagerAdapter pagerAdapter = new ProfilePagerAdapter(getFragmentManager());
        // add fragments to adapter
        pagerAdapter.addFragment(new MyStoriesFragment());
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

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(com.project.android_kidstories.ui.profile.ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

    private byte[] getImage(int clientId){
        byte[] user_image = helper.getUserImage(clientId);
        return user_image;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_edit_profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.profile_edit) {
            com.project.android_kidstories.ui.edit.ProfileFragment profileFragment = new com.project.android_kidstories.ui.edit.ProfileFragment();
            getFragmentManager().beginTransaction().replace(R.id.main_fragment_container, profileFragment).commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayProfile(){
        token = new SharePref(getActivity()).getMyToken();
        String firstname = new SharePref(getActivity()).getUserFirstname();
        String lastname = new SharePref(getActivity()).getUserLastname();
        String email = new SharePref(getActivity()).getUserEmail();
        Toast.makeText(getActivity(), token,Toast.LENGTH_LONG).show();

        String name = firstname + " " + lastname;

        userName.setText(name);
        userEmail.setText(email);

//        repository.getUserProfileApi().getUserProfile(token).enqueue(new Callback<BaseResponse<User>>() {
//            @Override
//            public void onResponse(Call<BaseResponse<User>> call, Response<BaseResponse<User>> response) {
//                if (response.isSuccessful()){
//                    assert response.body() != null;
//                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
//                    User user = response.body().getData();
//                    String name = user.getFirstName() + " " + user.getLastName();
//                    userName.setText(name);
//                    userEmail.setText(user.getEmail());
//                } else {
//                    assert response.errorBody() != null;
//                    String error = response.errorBody().toString();
//                    Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<BaseResponse<User>> call, Throwable t) {
//                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });

    }
}
