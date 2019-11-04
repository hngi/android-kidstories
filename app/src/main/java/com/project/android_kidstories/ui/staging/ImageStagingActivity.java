package com.project.android_kidstories.ui.staging;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.project.android_kidstories.R;
import com.project.android_kidstories.Utils.ImageConversion;
import com.project.android_kidstories.db.Helper.BedTimeDbHelper;
import com.takusemba.cropme.CropImageView;
import com.takusemba.cropme.CropLayout;
import com.takusemba.cropme.OnCropListener;

import java.io.FileNotFoundException;

public class ImageStagingActivity extends AppCompatActivity {

    private static final String INTENT_URI_KEY = "INTENT_URI_KEY";

    public static void start(Context context, String uriString) {
        Intent intent = new Intent(context, ImageStagingActivity.class);
        intent.putExtra(INTENT_URI_KEY, uriString);

        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_staging);

        CropImageView civ = findViewById(R.id.cropme_image_view);

        String uriString = getIntent().getStringExtra(INTENT_URI_KEY);
        if (uriString == null) {
            showMessage("No image received");
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
                byte[] imageByteArray = new ImageConversion().convertBitMapToByteArray(bitmap);
                helper.storeUserImage(imageByteArray, ImageStagingActivity.this);
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
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
