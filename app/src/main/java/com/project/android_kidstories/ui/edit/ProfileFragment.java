package com.project.android_kidstories.ui.edit;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.project.android_kidstories.R;

import java.io.FileNotFoundException;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {


    ImageView imageView;
    Button btn_upload;
    private static int RESULT_LOAD_IMAGE = 1;



    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_edit, container, false);

        imageView = root.findViewById(R.id.img_user);
        btn_upload = root.findViewById(R.id.btn_upload_image);
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent images = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(images, RESULT_LOAD_IMAGE);
            }
        });
        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selected_image = data.getData();
            //String image_text = selected_image.toString();
            Bitmap bitmap;
            try {
                bitmap = BitmapFactory.decodeStream(getContext().getContentResolver().openInputStream(selected_image));
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}