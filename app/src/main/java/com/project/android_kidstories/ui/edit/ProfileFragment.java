package com.project.android_kidstories.ui.edit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
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
import com.project.android_kidstories.Api.Api;
import com.project.android_kidstories.Api.Responses.BaseResponse;
import com.project.android_kidstories.Api.RetrofitClient;
import com.project.android_kidstories.DataStore.Repository;
import com.project.android_kidstories.Model.User;
import com.project.android_kidstories.R;
import com.project.android_kidstories.Utils.ImageConversion;
import com.project.android_kidstories.Views.main.MainActivity;
import com.project.android_kidstories.db.Helper.BedTimeDbHelper;
import com.project.android_kidstories.sharePref.SharePref;
import com.project.android_kidstories.viewModel.FragmentsSharedViewModel;

import java.io.File;
import java.io.FileNotFoundException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {


    private static final String TAG = "ProfileFragment";
    ImageView imageView;
    Button btnUpload;
    Button save;
    private static int RESULT_LOAD_IMAGE = 1;
    ImageConversion imageConversion;
    TextView username;
    TextView imagePath;

    BedTimeDbHelper helper;


    private FragmentsSharedViewModel viewModel;
    private Repository repository;
    private String token;
    private String imageFilePath;
    private String email;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        helper = new BedTimeDbHelper(getContext());

        imageConversion = new ImageConversion();

        View root = inflater.inflate(R.layout.fragment_edit, container, false);

        imagePath = root.findViewById(R.id.selected_image_path);
        imageView = root.findViewById(R.id.img_user);
        username = root.findViewById(R.id.tv_username);
        btnUpload = root.findViewById(R.id.btn_upload);
        btnUpload .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent images = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(images, RESULT_LOAD_IMAGE);
            }
        });

        save = root.findViewById(R.id.btn_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(username.getText().toString())){
                    //username.setError("Username cannot be empty");
                }else {
//                    Bitmap bitmap;
//                    if (imageView.getDrawable() instanceof BitmapDrawable) {
//                        bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
//                    } else {
//                        Drawable d = imageView.getDrawable();
//                        bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
//                        Canvas canvas = new Canvas(bitmap);
//                        d.draw(canvas);
//                    }
//
//                    byte[] image_byte_array = imageConversion.convertBitMapToByteArray(bitmap);
//                    helper.storeUserImage(image_byte_array, getContext());


                    //upload data to server

                    if (imageFilePath.isEmpty())return;

                    uploadToServer(imageFilePath);
                }
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(FragmentsSharedViewModel.class);

        username.setText(viewModel.currentUser.getFirstName() + " " + viewModel.currentUser.getLastName());
        if(viewModel.currentUser.getImage() != null){
            Glide.with(getActivity().getApplicationContext())
                    .load(viewModel.currentUser.getImage())
                    .into(imageView);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         // get user token
        getUserDetails();

        //init repository
        repository = Repository.getInstance(requireActivity().getApplication());

    }

    private void uploadToServer(String filePath) {


        //Create a file object using file path
        File file = new File(filePath);

        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);

        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("upload", file.getName(), fileReqBody);

        //Create request body with text description and text media type
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");

        //
        Call call = repository.getStoryApi().uploadUserImage("Bearer " + token,part,description);

        Toast.makeText(getActivity(),"image uploading pls wait ...",Toast.LENGTH_SHORT).show();


        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {

                if (response.isSuccessful()){

                    Toast.makeText(requireContext(),response.body().toString(),Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: "+response.body());

                    uploadUserName(repository.getStoryApi());

                }else{
                    Toast.makeText(requireContext(),response.body().toString(),Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(requireContext(),t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void uploadUserName(Api storyApi){

        String[] name = username.getText().toString().trim().split(" ");

        String fName = name[0];
        String lName = name[1];

        User user = new User(fName,lName,email);

        Toast.makeText(requireContext(),"name uploading pls wait...",Toast.LENGTH_SHORT).show();

        storyApi.updateUserProfile(token,user).enqueue(new Callback<BaseResponse<User>>() {
            @Override
            public void onResponse(Call<BaseResponse<User>> call, Response<BaseResponse<User>> response) {

                if (response.isSuccessful()){

                    Toast.makeText(requireContext(),response.body().toString(),Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: "+response.body());

                    uploadUserName(repository.getStoryApi());

                }else{
                    Toast.makeText(requireContext(),response.body().toString(),Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<BaseResponse<User>> call, Throwable t) {

                    Toast.makeText(requireContext(),t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getUserDetails(){
        token = new SharePref(requireContext()).getMyToken();
        email = new SharePref((requireContext())).getUserEmail();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            Uri selected_image = data.getData();
            imageFilePath = selected_image.toString();
            String image_text = selected_image.toString();

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
}