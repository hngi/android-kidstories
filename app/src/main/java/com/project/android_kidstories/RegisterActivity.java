package com.project.android_kidstories;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

public class RegisterActivity extends AppCompatActivity {

    EditText email;
    EditText phone;
    EditText fullName;
    EditText password;
    Button btn;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.reg_email);
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

    }

    private void signInUser() {
        String email_string = email.getText().toString();
        String phone_string = phone.getText().toString();
        String fullName_string = fullName.getText().toString();
        String password_string = password.getText().toString();

        //validating text fields

        if(TextUtils.isEmpty(email_string) || (!Patterns.EMAIL_ADDRESS.matcher(email_string).matches())){
            email.setError("Please enter a valid email address");
            return;
        }

        if(TextUtils.isEmpty(phone_string) || (!Patterns.PHONE.matcher(phone_string).matches())){
            phone.setError("Please enter a valid phone number");
            return;
        }

        if(TextUtils.isEmpty(fullName_string) || TextUtils.isEmpty(password_string)){
            fullName.setError("Please enter a valid phone number");
            password.setError("Enter a password");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
    }
}
