package com.project.android_kidstories.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.project.android_kidstories.DataStore.ApiViewmodel;
import com.project.android_kidstories.Model.User;
import com.project.android_kidstories.Views.main.MainActivity;
import com.project.android_kidstories.R;
import com.project.android_kidstories.sharePref.SharePref;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String USER_KEY_INTENT_EXTRA ="com.project.android_kidstories_USER_KEY";

    private static final String TAG = "LoginActivity";
    private GoogleSignInClient googleSignInClient;
    private Button googleSignInButton;
    private CallbackManager callbackManager;
    private static final String EMAIL = "email";
    private static final String AUTH_TYPE = "rerequest";
    private SharePref sharePref;
    ApiViewmodel apiViewmodel;
    Handler handler;
    EditText emailET;
    boolean isNowLoggedIn;
    EditText passwordET;
    Button btn;
    TextView register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        initViews();
        apiViewmodel= ViewModelProviders.of(this).get(ApiViewmodel.class);
        googleSignInButton = findViewById(R.id.google_auth_button);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
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

//        register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                gotoRegisterActivity();
//            }
//        });
        TextView createAccount = findViewById(R.id.create_account);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                loginUser();
//            }
//        });

        /* ******************* Facebook Authentication ********************** */
        FacebookSdk.sdkInitialize(this);
        callbackManager = CallbackManager.Factory.create();

        Button facebookAuthButton = findViewById(R.id.facebook_auth_button);
        facebookAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().setAuthType(AUTH_TYPE)
                        .logInWithReadPermissions(LoginActivity.this, Arrays.asList(EMAIL));
                facebookLogin();
            }
        });
    }

    public SharePref getSharePref() {
        return sharePref;
    }

    private void initViews() {
        emailET= findViewById(R.id.et_email);
        passwordET = findViewById(R.id.et_password);
        btn = findViewById(R.id.login_button);
        register =findViewById(R.id.create_account);

        register.setOnClickListener(this);
        btn.setOnClickListener(this);


    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button: loginViaRoom();
                break;
            case R.id.create_account:
                gotoRegisterActivity(LoginActivity.this);
                break;
        }
    }

    private void loginViaRoom() {
        showProgressbar();
        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();


        if (!validateEmailPassword(email, password)) {
            hideProgressbar();
            isNowLoggedIn =false;
            return;
        }

        for(User user:apiViewmodel.getRepository().getAllLocalUsers()){
            if(user.getEmail().equals(email)&&user.getPassword().equals(password)&&!isNowLoggedIn){
                isNowLoggedIn =true;
                getSharePref().setLoggedUserId(user.getId());
                Log.d(TAG, "register: SharedPref LoggedIn UserId "+getSharePref().getLoggedUserId());
                openMainActivityOnDelay(user);
                return;
            }else if(isNowLoggedIn){
                showToast("Please wait, Logging you in");
                return;
            }
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgressbar();
                showToast("Wrong email or password");
                isNowLoggedIn =false;
            }
        },1000);

    }

    private void openMainActivityOnDelay(final User user) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                openMainActivity(LoginActivity.this,user);
                finish();
            }
        },1000);
    }
    protected void openMainActivity(Context context, User currentUser) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MainActivity.USER_KEY_INTENT_EXTRA,currentUser);
        startActivity(intent);
    }

    private boolean validateEmailPassword(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            emailET.setError("Required");
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            passwordET.setError("Required");
            return false;
        } else if (password.length() > 15) {
            passwordET.setError("Password too long");
            return false;
        }
        return true;
    }

    //ProgressBar progressBar = findViewById(R.id.login_progress);
    private void showProgressbar() {
       // progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressbar() {
        //progressBar.setVisibility(View.GONE);
    }

    protected void gotoRegisterActivity(Context context) {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, RegisterActivity.class);
                startActivity(i);
            }
        });

    }

    private void loginUser() {
        String email_string = emailET.getText().toString();
        String password_string = passwordET.getText().toString();

        //validating text fields

        if (TextUtils.isEmpty(email_string) || (!Patterns.EMAIL_ADDRESS.matcher(email_string).matches())) {
            emailET.setError("Please enter a valid email address");
            return;
        }

        if (TextUtils.isEmpty(password_string)) {
            passwordET.setError("Please enter a password");
            return;
        }

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
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

                    } catch (ApiException e) {
                        // The ApiException status code indicates the detailed failure reason.
                        Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
                    }
                    break;
            }
        }
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }


    private void onLoggedIn(GoogleSignInAccount googleSignInAccount) {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }

    public void onStart() {
        super.onStart();
        checkLoginStatus();
        GoogleSignInAccount alreadyloggedAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (alreadyloggedAccount != null) {
            Toast.makeText(this, "Already Logged In", Toast.LENGTH_SHORT).show();
            onLoggedIn(alreadyloggedAccount);
        } else {
            Log.d(TAG, "Not logged in");
        }
    }

    private void checkLoginStatus() {
        if (AccessToken.getCurrentAccessToken() != null && !AccessToken.getCurrentAccessToken().isExpired()) {
            // user already signed in
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
    }

    public void facebookLogin() {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(LoginActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onSuccess: " + loginResult.getAccessToken().getUserId());
                setResult(RESULT_OK);
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
                /*call : loginResult.getAccessToken().getUserId() to get userId and save to database;*/
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "Error " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
