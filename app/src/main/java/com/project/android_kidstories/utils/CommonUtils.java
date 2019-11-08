package com.project.android_kidstories.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CommonUtils {

    public static Bitmap loadBitmap(Context context, String picName) {
        Bitmap b = null;
        FileInputStream fis;
        try {
            fis = context.openFileInput(picName);
            b = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            Log.d("tag", "file not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.d("tag", "io exception");
            e.printStackTrace();
        } finally {

        }
        return b;
    }


}
