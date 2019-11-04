package com.project.android_kidstories.ui.base;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import com.project.android_kidstories.data.source.local.preferences.SharePref;

public class BaseActivity extends AppCompatActivity {
    private SharePref sharePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharePref = SharePref.getINSTANCE(this);
    }

    public SharePref getSharePref() {
        return sharePref;
    }

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    protected void showSnack(String message, View root) {
        Snackbar.make(root, message, Snackbar.LENGTH_SHORT).show();
    }
}
