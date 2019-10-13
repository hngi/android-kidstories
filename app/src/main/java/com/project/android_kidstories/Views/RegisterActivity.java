package com.project.android_kidstories.Views;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
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

import com.project.android_kidstories.Views.main.MainActivity;
import com.project.android_kidstories.R;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    public static final int LOGIN_TEXT_REQUEST_CODE = 11;
    private CallbackManager callbackManager;
    private static final String EMAIL = "email";
    private static final String AUTH_TYPE = "rerequest";

    EditText emailET;
    EditText phone;
    EditText fullName;
    EditText password, confirmPassword;
    Button regFacebook, regGoogle, SignUp;
    TextView loginText;
    ProgressBar progressBar;


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

        phone = findViewById(R.id.reg_contact);
        password = findViewById(R.id.reg_password);
        fullName = findViewById(R.id.reg_full_name);
        emailET = findViewById(R.id.reg_email);
        confirmPassword = findViewById(R.id.reg_confirm_password);

        regFacebook = findViewById(R.id.reg_facebook);
        regGoogle = findViewById(R.id.reg_google);
        SignUp = findViewById(R.id.sign_up_button);
        loginText = findViewById(R.id.create_act);

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

        // if user is already registered
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(RegisterActivity.this, LoginActivity.class), LOGIN_TEXT_REQUEST_CODE);
            }
        });
    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                fullName.setText("");
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
                    String email = object.getString("email");
                    String id = object.getString("id");

                    String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

                    emailET.setText(email);

                    fullName.setText(First_Name + " " + Last_Name);
                    RequestOptions requestOptions = new RequestOptions();
                    requestOptions.dontAnimate();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "First_Name,Last_Name,email,id");
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



















     /*  email = findViewById(R.id.reg_email);
        phone = findViewById(R.id.reg_contact);
        fullName = findViewById(R.id.reg_full_name);
        password = findViewById(R.id.reg_password);
        btn = findViewById(R.id.sign_up_button);
        progressBar =  findViewById(R.id.reg_progress_bar);
        btn.setOnClickListener(new View.OnClickListener() {
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

    private void signInUser() {
        String email_string = emailET.getText().toString();
        String phone_string = phone.getText().toString();
        String fullName_string = fullName.getText().toString();
        String password_string = password.getText().toString();

        //validating text fields

        if (TextUtils.isEmpty(email_string) || (!Patterns.EMAIL_ADDRESS.matcher(email_string).matches())) {
            emailET.setError("Please enter a valid email address");
            return;
        }

        if (TextUtils.isEmpty(phone_string) || (!Patterns.PHONE.matcher(phone_string).matches())) {
            phone.setError("Please enter a valid phone number");
            return;
        }

        if (TextUtils.isEmpty(fullName_string) || TextUtils.isEmpty(password_string)) {
            fullName.setError("Please enter a valid phone number");
            password.setError("Enter a password");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
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
}

