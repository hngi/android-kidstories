package com.project.android_kidstories.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;

/**
 * Convert images from Bitmap to ByteArray and vice-versa
 */

public class ImageConversion {

    /**
     * convert bitmap to byte array for database storage
     * @param bitmap
     * @return
     */
    public byte[] convertBitMapToByteArray(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    /**
     * convert byte array to bitmap
     * @param image
     * @return
     */
    public Bitmap convertByteArraytoBitMap(byte[] image){
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        return bitmap;
    }

    /**
     * fit bitmap to imageView
     * @param image
     * @param imageViewHeight
     * @param imageViewWidth
     * @return
     */
    public Bitmap fitBitMaptoImageView(Bitmap image, int imageViewHeight, int imageViewWidth){
        Bitmap scaledImage = Bitmap.createScaledBitmap(image, imageViewWidth, imageViewHeight, true);
        return scaledImage;
    }

    /**
     * get image uri from bitmap
     * @param inContext
     * @param inImage
     * @return
     */
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
