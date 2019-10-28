package com.project.android_kidstories.ui.edit;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import com.bumptech.glide.Glide;
import com.project.android_kidstories.Api.Responses.BaseResponse;
import com.project.android_kidstories.DataStore.Repository;
import com.project.android_kidstories.Model.User;
import com.project.android_kidstories.R;
import com.project.android_kidstories.Utils.ImageConversion;
import com.project.android_kidstories.db.Helper.BedTimeDbHelper;
import com.project.android_kidstories.sharePref.SharePref;
import com.project.android_kidstories.viewModel.FragmentsSharedViewModel;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {


    ImageView imageView;
    Button btnUpload;
    Button save;
    private static int RESULT_LOAD_IMAGE = 1;
    ImageConversion imageConversion;
    TextView imagePath, userName;
    String token;

    BedTimeDbHelper helper;
    Uri selected_image;


    private FragmentsSharedViewModel viewModel;
    private Repository repository;
    private String mediaPath;

    private static final int REQUEST_WRITE_PERMISSION = 786;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        helper = new BedTimeDbHelper(getContext());

        viewModel = ViewModelProviders.of(getActivity()).get(FragmentsSharedViewModel.class);

        String fName = new SharePref((requireContext())).getUserFirstname();
        String lName = new SharePref((requireContext())).getUserLastname();
        String email = new SharePref((requireContext())).getUserEmail();

        viewModel.setUser(new User(fName,lName,email));
        repository = Repository.getInstance(getActivity().getApplication());

        imageConversion = new ImageConversion();

        View root = inflater.inflate(R.layout.fragment_edit, container, false);

        userName = root.findViewById(R.id.tv_username);
        imageView = root.findViewById(R.id.img_user);
        displayUsersInfo();
        imagePath = root.findViewById(R.id.selected_image_path);
        btnUpload = root.findViewById(R.id.btn_upload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //request for permission if not granted
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_PERMISSION);
                } else {
                    Intent images = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(images, RESULT_LOAD_IMAGE);
                }


            }
        });

        save = root.findViewById(R.id.btn_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

                    File file = new File(mediaPath);

                    // create RequestBody instance from file
                    RequestBody requestFile =
                            RequestBody.create(
                                    file,
                                    MediaType.parse(getActivity().getContentResolver().getType(selected_image))
                            );

                    MultipartBody.Part part = MultipartBody.Part.createFormData("photo", file.getName(),requestFile);
                    repository.getStoryApi().updateUserProfilePicture("Bearer" + token,
                            part).enqueue(new Callback<BaseResponse<Void>>() {
                        @Override
                        public void onResponse(Call<BaseResponse<Void>> call, Response<BaseResponse<Void>> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(requireContext(),"upload successful",Toast.LENGTH_SHORT).show();
                                Log.d("Upload State", "Successful");
                                Log.d("Upload State", response.body().getMessage());
                            } else {
                                Log.d("Upload Status", "Something went wrong");
                            }
                        }

                        @Override
                        public void onFailure(Call<BaseResponse<Void>> call, Throwable t) {
                            Log.d("Upload Status", "Network Failure");
                            Log.d("Upload Status", t.getMessage());
                        }
                    });
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(FragmentsSharedViewModel.class);
        repository = Repository.getInstance(getActivity().getApplication());
        token = new SharePref(getActivity()).getMyToken();

        if (viewModel.currentUser.getImage() != null) {
            Glide.with(getActivity().getApplicationContext())
                    .load(viewModel.currentUser.getImage())
                    .into(imageView);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent images = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(images, RESULT_LOAD_IMAGE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            selected_image = data.getData();
            String image_text = selected_image.toString();

            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selected_image, filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            mediaPath = cursor.getString(columnIndex);
            cursor.close();

            imagePath.setText(image_text);
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

    public void displayUsersInfo(){
        String firstname = new SharePref(getActivity()).getUserFirstname();
        String lastname = new SharePref(getActivity()).getUserLastname();

        String name = firstname + " " + lastname;

        if (viewModel.currentUser.getLastName() != null && viewModel.currentUser.getFirstName() != null
                && viewModel.currentUser.getEmail() != null) {
            userName.setText(viewModel.currentUser.getFirstName() + " " + viewModel.currentUser.getLastName());
        } else {
            userName.setText(name);
        }
    }
}