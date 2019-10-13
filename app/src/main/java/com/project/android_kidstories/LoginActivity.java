package com.project.android_kidstories;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.project.android_kidstories.DataStore.ApiViewmodel;
import com.project.android_kidstories.DataStore.Repository;
import com.project.android_kidstories.Model.User;
import com.project.android_kidstories.Utils.Common;
import com.project.android_kidstories.base.BaseActivity;

import java.util.List;

import static java.lang.System.in;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "AndroidClarified";
    private GoogleSignInClient googleSignInClient;
    private Button googleSignInButton;
    private ApiViewmodel apiViewmodel;
    EditText login_email;
    EditText login_password;
    Button login_btn;
    TextView register;
    Handler handler;
    int RC_SIGN_IN = 0;
    private Repository repository;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        initViews();
        // Configure sign-in to request the user's ID, login_email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getResources().getString(R.string.web_client_id))
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleSignIn();
//                Intent signInIntent = googleSignInClient.getSignInIntent();
//                startActivityForResult(signInIntent, 101);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 101);
            }
        });

    }

    private void initViews() {
        login_email = findViewById(R.id.et_email);
        login_password = findViewById(R.id.et_password);
        login_btn = findViewById(R.id.login_button);
        register = findViewById(R.id.create_account);
        googleSignInButton = findViewById(R.id.google_auth_button);
        hideProgressbar();
        googleSignInButton.setOnClickListener(this);
        login_btn.setOnClickListener(this);
        register.setOnClickListener(this);
        progressBar = findViewById(R.id.login_progress);

        ApiViewmodel apiViewmodel= ViewModelProviders.of(this).get(ApiViewmodel.class);
        repository = apiViewmodel.getRepository();
        handler = new Handler();
    }

    private void loginUser() {
        String email_string = login_email.getText().toString();
        String password_string = login_password.getText().toString();

        //validating text fields

        if(TextUtils.isEmpty(email_string) || (!Patterns.EMAIL_ADDRESS.matcher(email_string).matches())){
            login_email.setError("Please enter a valid login_email address");
            return;
        }

        if (TextUtils.isEmpty(password_string)) {
            login_password.setError("Please enter a login_password");
            return;
        }
        //User user=new User(Parcel in);
       /* user.setEmail(email_string);
        user.setPassword(password_string);*/
        List<User> allLocalUsers = repository.getAllLocalUsers();
        for(User user1:allLocalUsers){
            if(user1.getPassword().equals(password_string)&&user1.getEmail().equals(email_string)){
                openMainActivity(this,user1);
            }
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Google Sign In Error", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_LONG).show();
            hideProgressbar();
        }
    }

    private void googleSignIn() {
        if(!Common.checkNetwork(this)){
            showToast("You do not have internet connection, Check internet or log in with email");
            return;
        }
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        showProgressbar();
    }

    private void onLoggedIn(GoogleSignInAccount googleSignInAccount) {
        Intent intent = new Intent(this, ProfileActivity.class);


        startActivity(intent);
        finish();
    }
//    public void onStart() {
//        super.onStart();
//        GoogleSignInAccount alreadyloggedAccount = GoogleSignIn.getLastSignedInAccount(this);
//        if (alreadyloggedAccount != null) {
//            Toast.makeText(this, "Already Logged In", Toast.LENGTH_SHORT).show();
//            onLoggedIn(alreadyloggedAccount);
//        } else {
//            Log.d(TAG, "Not logged in");
//        }
//    }

    @Override
    protected void onStart() {
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount alreadyloggedIn = GoogleSignIn.getLastSignedInAccount(this);
        if (alreadyloggedIn != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        } else if (getSharePref().getLoggedUserId() != -1) {
            for (User looggedUser : apiViewmodel.getRepository().getAllLocalUsers()) {
                if(looggedUser.getId().equals(getSharePref().getLoggedUserId())) {
                    openMainActivity(LoginActivity.this,looggedUser);
                    finish();
                }
            }
        }

        super.onStart();
    }


    private void showProgressbar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressbar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                loginUser();
                break;
            case R.id.create_account:
                gotoRegisterActivity(LoginActivity.this);
                break;
        }
    }

}
