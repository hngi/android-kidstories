package com.project.android_kidstories.ui.staging;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import androidx.fragment.app.Fragment;
import com.project.android_kidstories.R;
import com.project.android_kidstories.data.source.helpers.BedTimeDbHelper;
import com.project.android_kidstories.ui.base.BaseActivity;
import com.takusemba.cropme.CropImageView;
import com.takusemba.cropme.CropLayout;
import com.takusemba.cropme.OnCropListener;

import java.io.FileNotFoundException;

public class ImageStagingActivity extends BaseActivity {

    private static final String INTENT_URI_KEY = "INTENT_URI_KEY";
    private static final String IMAGE_BITMAP_KEY = "IMAGE_BITMAP_KEY";

    public static void startForResult(Fragment fragment, String uriString, int request_code) {
        Intent intent = new Intent(fragment.requireContext(), ImageStagingActivity.class);
        intent.putExtra(INTENT_URI_KEY, uriString);

        fragment.startActivityForResult(intent, request_code);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_staging);

        CropImageView civ = findViewById(R.id.cropme_image_view);

        String uriString = getIntent().getStringExtra(INTENT_URI_KEY);
        if (uriString == null) {
            showMessage("No image received");

            setResult(Activity.RESULT_CANCELED);
            finish();
        }

        Uri uri = Uri.parse(uriString);

        try {

            Bitmap bitmap;
            bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(uri));
            civ.setImageBitmap(bitmap);

        } catch (FileNotFoundException fnfe) {
            showMessage("No image received");
        }

    }

    public void saveCropped(View view) {
        BedTimeDbHelper helper = new BedTimeDbHelper(this);

        CropLayout crop_view = findViewById(R.id.crop_view);
        crop_view.crop(new OnCropListener() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                Intent data = new Intent();
                data.putExtra(IMAGE_BITMAP_KEY, bitmap);
                setResult(Activity.RESULT_OK, data);
                finish();
            }

            @Override
            public void onFailure() {
                showMessage("Image cropping failed");
            }
        });
    }

    public void cancelCropping(View view) {
        finish();
    }


    private void showMessage(String message) {
        showToast(message);
    }
}
