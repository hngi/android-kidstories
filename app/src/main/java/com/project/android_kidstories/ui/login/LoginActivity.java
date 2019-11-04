package com.project.android_kidstories.ui.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.facebook.*;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.project.android_kidstories.Api.Responses.loginRegister.LoginResponse;
import com.project.android_kidstories.DataStore.Repository;
import com.project.android_kidstories.R;
import com.project.android_kidstories.data.model.User;
import com.project.android_kidstories.data.source.local.preferences.SharePref;
import com.project.android_kidstories.ui.MainActivity;
import com.project.android_kidstories.viewModel.UserViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    public static final String USER_KEY_INTENT_EXTRA ="com.project.android_kidstories_USER_KEY";

    private static final String TAG = "LoginActivity";
    private GoogleSignInClient googleSignInClient;
    private Button googleSignInButton;
    private CallbackManager callbackManager;
    private static final String EMAIL = "email";
    private static final String AUTH_TYPE = "rerequest";
    EditText email;
    EditText password;
    Button btn;
    // ProgressDialog LoginProgress;
    TextView createAccount;
    ProgressBar loginProg;
    UserViewModel viewModel;
    SharedPreferences sharedPreferences;
    SharePref sharePref;

    private Repository repository;
    boolean isLogedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        repository = Repository.getInstance(getApplication());

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade__in);
        Animation transit = AnimationUtils.loadAnimation(this, R.anim.ttb);

        TextView transText = findViewById(R.id.welcome);
        TextView transText2 = findViewById(R.id.welcome);
        ImageView fadeInImage = findViewById(R.id.imageMain);

        transText.startAnimation(transit);
        transText2.startAnimation(transit);
        fadeInImage.startAnimation(fadeIn);

        loginProg = findViewById(R.id.login_progress);
        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        btn = findViewById(R.id.login_button);

        // LoginProgress = new ProgressDialog(LoginActivity.this);

        googleSignInButton = findViewById(R.id.google_auth_button);
        sharedPreferences = getSharedPreferences("API DETAILS", Context.MODE_PRIVATE);
        sharePref = new SharePref(this);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.


      /*  GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getResources().getString(R.string.web_client_id))
                .requestServerAuthCode("473866473162-4k87knredq3nnb19d4el239n1ja6r3ae.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);*/

        googleSignInSetUp();


        createAccount = findViewById(R.id.create_account);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
                overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);

            }
        });

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

        Log.e("TAG", isLogedIn +"");

    }

    private void googleSignInSetUp() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getResources().getString(R.string.web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);
                overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);

            }
        });

    }

    private void saveUserDetails(String token, String fullname, String email) {
        new SharePref(this).saveLoginDetails(token, fullname, email);
    }

    private void loginUser() {
        String email_string = email.getText().toString();
        String password_string = password.getText().toString();

        //validating text fields

        if (TextUtils.isEmpty(email_string) || (!Patterns.EMAIL_ADDRESS.matcher(email_string).matches())) {
            email.setError("Please enter a valid email address");
            return;
        } else if (TextUtils.isEmpty(password_string)) {
            password.setError("Please enter a password");
            return;

        } else {
            //  LoginProgress.setTitle("Signing In");
            //LoginProgress.setMessage("Please wait...");
            //LoginProgress.setCanceledOnTouchOutside(false);
            //LoginProgress.show();
            loginProg.setVisibility(View.VISIBLE);
            repository.getStoryApi().loginUser(email_string, password_string).enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {


                    if (response.isSuccessful()) {

                        assert response.body() != null;
                        String token = response.body().getUser().getToken();
                        //Toast.makeText(LoginActivity.this, token, Toast.LENGTH_LONG).show();

                        String mFirstname = response.body().getUser().getFirstName();
                        String mLastname = response.body().getUser().getLastName();
                        String mEmail = response.body().getUser().getEmail();
                        saveUserDetails(token, mFirstname + " " + mLastname, mEmail);

                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString("Token", token);
                        editor.apply();
                        sharePref.setIsUserLoggedIn(true);
                        loginProg.setVisibility(View.INVISIBLE);
                        //   LoginProgress.dismiss();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("myToken", token);
                        startActivity(intent);
                        finish();

                    } else {
                        loginProg.setVisibility(View.INVISIBLE);
                        // LoginProgress.hide();
                        Snackbar.make(findViewById(R.id.login_parent_layout), "Invalid Username or Password"
                                , Snackbar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    loginProg.setVisibility(View.INVISIBLE);
                    // LoginProgress.hide();
                    Snackbar.make(findViewById(R.id.login_parent_layout), "Network Failure"
                            , Snackbar.LENGTH_LONG).show();

                }
            });
        }


    }

    @Override
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
                        onLoggedIn(account);
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

        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            String idToken = account.getIdToken();

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            // updateUI(null);
        }
               /* Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            } catch (ApiException e) {
                // The ApiException status code indicates the detailed failure reason.
                // Please refer to the GoogleSignInStatusCodes class reference for more information.
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
               // updateUI(null);
            }
        }*/


    }


    private void onLoggedIn(GoogleSignInAccount googleSignInAccount) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onStart() {

        GoogleSignInAccount alreadyloggedAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (alreadyloggedAccount != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            Toast.makeText(this, "Already Logged In", Toast.LENGTH_SHORT).show();

        } else if (getSharePref().getUserId() != -1) {
            for (User loggedUser : viewModel.getallUsers() ) {
                if (loggedUser.getId().equals(getSharePref().getLoggedUserId())){
                    openMainActivity(LoginActivity.this, loggedUser);
                    finish();
                }
            }


            Log.d(TAG, "Not logged in");
        }
        super.onStart();
        // Check if user is logged in through facebook
        checkLoginStatus();


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

    public SharePref getSharePref() {
        return sharePref;
    }

    protected void openMainActivity(Context context, User currentUser) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MainActivity.USER_KEY_INTENT_EXTRA, (Parcelable) currentUser);
        startActivity(intent);
    }
}


