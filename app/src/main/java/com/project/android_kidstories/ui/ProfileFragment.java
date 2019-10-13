package com.project.android_kidstories.ui;

import androidx.lifecycle.ViewModelProviders;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.project.android_kidstories.R;
import com.project.android_kidstories.Utils.ImageConversion;
import com.project.android_kidstories.db.Helper.AddUsers;
import com.project.android_kidstories.db.Helper.BedTimeDbHelper;

public class ProfileFragment extends Fragment {
    public ImageView imageView;
    BedTimeDbHelper helper;
    ImageConversion imageConversion;

    private ProfileViewModel mViewModel;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        helper = new BedTimeDbHelper(getContext());
        imageConversion = new ImageConversion();

        int client_id = helper.getLastId(AddUsers.AddUsersColumn.TABLE_NAME);

        View root = inflater.inflate(R.layout.profile_fragment, container, false);

        imageView = root.findViewById(R.id.profile);

        Bitmap image = imageConversion.convertByteArraytoBitMap(getImage(client_id));
        Bitmap resizedImage = imageConversion.fitBitMaptoImageView(image, 178, 178);
        imageView.setImageBitmap(resizedImage);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

    private byte[] getImage(int clientId){
        byte[] user_image = helper.getUserImage(clientId);
        return user_image;
    }
}
