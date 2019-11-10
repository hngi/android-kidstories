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
import com.project.android_kidstories.ui.base.BaseActivity;
import com.takusemba.cropme.CropImageView;
import com.takusemba.cropme.CropLayout;
import com.takusemba.cropme.OnCropListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageStagingActivity extends BaseActivity {

    private static final String INTENT_URI_KEY = "INTENT_URI_KEY";
    public static final String IMAGE_BITMAP_KEY = "IMAGE_BITMAP_KEY";
    public static final String IMAGE_URI_KEY = "IMAGE_URI_KEY";

    public static void startForResult(Fragment fragment, String uriString, int request_code) {
        Intent intent = new Intent(fragment.requireContext(), ImageStagingActivity.class);
        intent.putExtra(INTENT_URI_KEY, uriString);

        fragment.startActivityForResult(intent, request_code);
    }

    String uriStr;

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

        uriStr = uriString;
        Uri uri = Uri.parse(uriStr);

        try {
            Bitmap compressed = compressBitmap(uri);
            if (compressed == null) {
                showToast("Could not compress image");
                finish();
            }

            civ.setImageBitmap(compressed);

        } catch (IOException ioe) {
            showMessage("No image received");
        }
    }

    private Bitmap compressBitmap(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        if (inputStream == null) {
            return null;
        }

        int width = 250;
        int height = 250;

        BitmapFactory.Options scaleOptions = new BitmapFactory.Options();
        scaleOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeStream(inputStream, null, scaleOptions);

        inputStream.close();
        inputStream = getContentResolver().openInputStream(uri);
        if (inputStream == null) {
            return null;
        }

        int scale = 1;
        while (scaleOptions.outWidth / scale / 2 >= width && scaleOptions.outHeight / scale / 2 >= height) {
            scale *= 2;
        }

        BitmapFactory.Options outOptions = new BitmapFactory.Options();
        outOptions.inJustDecodeBounds = false;
        outOptions.inSampleSize = scale;

        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, outOptions);
        if (bitmap == null) {
            return null;
        }

        inputStream.close();

        ByteArrayOutputStream baoStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.WEBP, 100, baoStream);

        byte[] byteArray = baoStream.toByteArray();
        baoStream.close();

        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    public void saveCropped(View view) {
        CropLayout crop_view = findViewById(R.id.crop_view);
        crop_view.crop(new OnCropListener() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                Intent data = new Intent();
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, bs);

                data.putExtra(IMAGE_BITMAP_KEY, bs.toByteArray());
                data.putExtra(IMAGE_URI_KEY, uriStr);
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
