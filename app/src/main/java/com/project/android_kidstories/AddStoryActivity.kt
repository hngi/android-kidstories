package com.project.android_kidstories

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_add_story.*
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlin.math.PI


const val PICTURE_REQUEST_CODE = 100
const val PERMISSION_REQUEST_CODE = 200

class AddStoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_story)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        btn_upload.setOnClickListener {
            choosePicture()
        }
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)

        }
    }

    private fun choosePicture() {

        checkPermission()

        val pictureIntent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }

        startActivityForResult(pictureIntent, PICTURE_REQUEST_CODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK ){
            if (requestCode == PICTURE_REQUEST_CODE && data != null){
                val imageUri = data.data
                val imagePath = imageUri?.path
                textView3.text = imagePath
            }
        }
    }
}
