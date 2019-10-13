package com.project.android_kidstories.ui.edit;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.project.android_kidstories.R;
import com.project.android_kidstories.Utils.ImageConversion;
import com.project.android_kidstories.db.Helper.BedTimeDbHelper;

import java.io.FileNotFoundException;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {


    ImageView imageView;
    Button btn_upload, save;
    private static int RESULT_LOAD_IMAGE = 1;
    ImageConversion imageConversion;
    EditText username;

    BedTimeDbHelper helper;


    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        helper = new BedTimeDbHelper(getContext());

        imageConversion = new ImageConversion();

        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_edit, container, false);

        imageView = root.findViewById(R.id.img_user);
        username = root.findViewById(R.id.ed_username);
        btn_upload = root.findViewById(R.id.btn_upload_image);
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent images = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(images, RESULT_LOAD_IMAGE);
            }
        });

        save = root.findViewById(R.id.btn_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(username.getText().toString())){
                    username.setError("Username cannot be empty");
                }else {
                    Bitmap bitmap;
                    if (imageView.getDrawable() instanceof BitmapDrawable) {
                        bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    } else {
                        Drawable d = imageView.getDrawable();
                        bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmap);
                        d.draw(canvas);
                    }

                    byte[] image_byte_array = imageConversion.convertBitMapToByteArray(bitmap);
                    helper.storeUserImage(image_byte_array, getContext());
                }
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