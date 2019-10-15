package com.project.android_kidstories.Views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.request.RequestOptions;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Objects;

import com.project.android_kidstories.Api.Api;
import com.project.android_kidstories.DataStore.ApiViewmodel;
import com.project.android_kidstories.Model.User;
import com.project.android_kidstories.Views.main.MainActivity;
import com.project.android_kidstories.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "RegisterActivity";
    public static final int LOGIN_TEXT_REQUEST_CODE = 11;
    private CallbackManager callbackManager;
    private static final String EMAIL = "email";
    private static final String AUTH_TYPE = "rerequest";

    EditText emailET;
    EditText phoneeET;
    EditText fullNameET;
    EditText passWordET;
    EditText confirMPasswordET;
    Button regFacebook, regGoogle, SignUp;
    TextView loginText;
    ProgressBar progressBar;
    Button register_btn;
    ApiViewmodel viewmodel;
    Handler handler;
    private boolean isNowRegistered;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_TEXT_REQUEST_CODE) {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        printHashKey(this);
        checkLoginStatus();

        initViews();


    }

    private void initViews() {
        phoneeET = findViewById(R.id.reg_contact);
        passWordET = findViewById(R.id.reg_password);
        fullNameET = findViewById(R.id.reg_full_name);
        emailET = findViewById(R.id.reg_email);
        confirMPasswordET = findViewById(R.id.reg_confirm_password);
        register_btn = findViewById(R.id.sign_up_button);
        regFacebook = findViewById(R.id.reg_facebook);
        regGoogle = findViewById(R.id.reg_google);
        SignUp = findViewById(R.id.sign_up_button);
        loginText = findViewById(R.id.create_act);

        loginText.setOnClickListener(this);
        register_btn.setOnClickListener(this);

        handler = new Handler();
        viewmodel = ViewModelProviders.of(this).get(ApiViewmodel.class);
        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();
        regFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().setAuthType(AUTH_TYPE)
                        .logInWithReadPermissions(RegisterActivity.this, Arrays.asList(EMAIL));
                facebookLogin();
            }
        });

//        //Register
//        register_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                signInUser();
//                //if ()
//
//            }
//        });

        // if user is already registered
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(RegisterActivity.this, LoginActivity.class), LOGIN_TEXT_REQUEST_CODE);
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.create_act: gotoLoginActivity(this);
                break;
            case R.id.sign_up_button: register();
                break;
        }
    }


    private void register(){
        showProgressbar();
        String fName = Objects.requireNonNull(fullNameET.getText()).toString().trim();
        String email = Objects.requireNonNull(emailET.getText()).toString().trim();
        String password = Objects.requireNonNull(passWordET.getText()).toString().trim();
        String phone = Objects.requireNonNull(phoneeET.getText()).toString().trim();
        String confirmPassword = Objects.requireNonNull(confirMPasswordET.getText()).toString().trim();




        if (!validateInputs( fName, email, password,phone,confirmPassword)){
            hideProgressbar();
            return;
        }

//
        User newUser=new User(fName,email,password);
        newUser.setPassword(password);
        Long insertedUserId = viewmodel.getRepository().getUser(newUser);     //insertUser(newUser);
        newUser.setId(insertedUserId);

        if(insertedUserId!=null&&insertedUserId!=-1&&!isNowRegistered){
            isNowRegistered=true;
            //getSharePref().setLoggedUserId(insertedUserId);
            //Log.d(TAG, "register_btn: SharedPref LoggedIn UserId "+getSharePref().getLoggedUserId());
            openHandler(newUser);
        } else if(isNowRegistered){
            showToast("Registering Please wait");
            return;
        }else {
            showToast("Error Registering");
        }

    }

    private boolean validateInputs(String fname, String email, String password, String confirm_password,String phone) {
        if (TextUtils.isEmpty(fname)) {
            fullNameET.setError("Required");
            return false;
        }

        if (TextUtils.isEmpty(email)) {
            emailET.setError("Required");
            return false;
        }
        if (TextUtils.isEmpty(phone)) {
            phoneeET.setError("Required");
            return false;
        }
        if (TextUtils.isEmpty(confirm_password)) {
            confirMPasswordET.setError("Required");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            passWordET.setError("Required");
            return false;
        } else if (password.length() > 15) {
            passWordET.setError("Password too long");
            return false;
        }
        return true;
    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                fullNameET.setText("");
                emailET.setText("");
                Toast.makeText(RegisterActivity.this, "User Logged Out", Toast.LENGTH_LONG).show();

            } else {
                //loaduserprofile(currentAccessToken);
            }

        }
    };

    private void loaduserprofile(AccessToken newAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String First_Name = object.getString("First_Name");
                    String Last_Name = object.getString("Last_Name");
                    String email = object.getString("login_email");
                    String id = object.getString("id");

                    String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

                    emailET.setText(email);

                    fullNameET.setText(First_Name + " " + Last_Name);
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.dontAnimate();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "First_Name,Last_Name,login_email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private void checkLoginStatus() {
        if (AccessToken.getCurrentAccessToken() != null && !AccessToken.getCurrentAccessToken().isExpired()) {
            // user already signed in
            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            finish();
        }
    }

    public void facebookLogin() {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(RegisterActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onSuccess: " + loginResult);
                setResult(RESULT_OK);
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();
                /*call : loginResult.getAccessToken().getUserId() to get userId and save to database;*/
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(RegisterActivity.this, "Error " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }



     /*  login_email = findViewById(R.id.reg_email);
        phone = findViewById(R.id.reg_contact);
        fullName = findViewById(R.id.reg_full_name);
        login_password = findViewById(R.id.reg_password);
        login_btn = findViewById(R.id.sign_up_button);
        progressBar =  findViewById(R.id.reg_progress_bar);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInUser();
            }
        });

        googleSignInButton = findViewById(R.id.reg_google);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getResources().getString(R.string.web_client_id))
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case 101:
                    try {
                        // The Task returned from this call is always completed, no need to attach
                        // a listener.
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        String idToken = account.getIdToken();
                    /*
                      send this id token to server using HTTPS
                     */

//                    } catch (ApiException e) {
//                        // The ApiException status code indicates the detailed failure reason.
//                        Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
//                    }
//                    break;
//            }
//
//    }

    private boolean signInUser() {
        String email_string = emailET.getText().toString();
        String phone_string = phoneeET.getText().toString();
        String fullName_string = fullNameET.getText().toString();
        String password_string = passWordET.getText().toString();
        String confirm_password = confirMPasswordET.getText().toString();

        //validating text fields
        if (TextUtils.isEmpty(fullName_string) || TextUtils.isEmpty(fullName_string)) {
            emailET.setError("Please enter a valid login_email address");
            return false;
        }

        if (TextUtils.isEmpty(email_string) || (!Patterns.EMAIL_ADDRESS.matcher(email_string).matches())) {
            emailET.setError("Please enter a valid login_email address");
            return false;
        }

        if (TextUtils.isEmpty(phone_string) || (!Patterns.PHONE.matcher(phone_string).matches())) {
            phoneeET.setError("Please enter a valid phone number");
            return false;
        }

        if (TextUtils.isEmpty(password_string) || TextUtils.isEmpty(password_string)) {
            //fullName.setError("Please enter a valid phone number");
            phoneeET.setError("Enter a login_password");
            return false;
        }
        if (TextUtils.isEmpty(confirm_password) || TextUtils.isEmpty(confirm_password)) {
            confirMPasswordET.setError("Password not match");
            return false;
        }
            return true;
       // progressBar.setVisibility(View.VISIBLE);
    }
    private void openHandler(final User newUser) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgressbar();
                showToast("Registration Successful");
                gotoLoginActivity(RegisterActivity.this);

                finish();
            }
        },2000);
    }



    // Getting app hash key for facebook login registration
    private static void printHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey: " + hashKey + "=");
            }
        } catch (Exception e) {
            Log.e(TAG, "printHashKey: Error: " + e.getMessage());
        }
    }

    private void showProgressbar(){
        //progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressbar(){
        //progressBar.setVisibility(View.GONE);
    }

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    protected void gotoLoginActivity(Context context) {
        startActivity(new Intent(context, LoginActivity.class));
    }


}

