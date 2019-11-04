package com.project.android_kidstories.ui.staging;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.project.android_kidstories.R;
import com.takusemba.cropme.CropLayout;
import com.takusemba.cropme.OnCropListener;

public class ImageStagingActivity extends AppCompatActivity {

    private static void start(Context context) {
        Intent intent = new Intent(context, ImageStagingActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_staging);
    }

    public void saveCropped(View view) {
        CropLayout crop_view = findViewById(R.id.crop_view);
        crop_view.crop(new OnCropListener() {
            @Override
            public void onSuccess(Bitmap bitmap) {

            }

            @Override
            public void onFailure() {

            }
        });
    }

    public void cancelCropping(View view) {
        finish();
    }
}
