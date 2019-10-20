package com.project.android_kidstories;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.content.CursorLoader;

import com.project.android_kidstories.R;
import com.project.android_kidstories.Views.main.MainActivity;


public class AddStoryActivity extends AppCompatActivity {
    public final int PERMISSION_REQUEST_CODE = 100;
    public final int PICTURE_REQUEST_CODE = 200;
    private TextView imagePathText;
    private EditText storyTitle;
    private Uri imageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story);

        imagePathText = findViewById(R.id.textView3);

        Button uploadButton = findViewById(R.id.btn_upload);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
            }
        });

        storyTitle = findViewById(R.id.title_edt);
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    private void choosePicture() {
        checkPermission();

        Intent pictureIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        pictureIntent.addCategory(Intent.CATEGORY_OPENABLE);
        pictureIntent.setType("image/*");
        startActivityForResult(pictureIntent, PICTURE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICTURE_REQUEST_CODE && data != null) {
                imageUri = data.getData();
                String imagePath = imageUri.getPath();
                imagePathText.setText(imagePath);
            }
        }
    }

    public void TypeContent(View view){

        if(TextUtils.isEmpty(storyTitle.getText())){
            storyTitle.setError("Title cannot be empty");
        }else {
            Intent i = new Intent(AddStoryActivity.this, AddStoriesContentActivity.class);
            i.putExtra("story_title", storyTitle.getText().toString());


            if(imageUri != null)
            i.putExtra("image_uri",getPath(imageUri));

            startActivity(i);
        }

    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    public void onBackPressed() {
        Intent home = new Intent(getApplicationContext(), MainActivity.class);
        home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity(home);
    }

}
