package com.project.android_kidstories;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.flutterwave.raveandroid.RaveConstants;
import com.flutterwave.raveandroid.RavePayActivity;
import com.flutterwave.raveandroid.RavePayManager;

import java.util.UUID;

public class DonateActivity extends AppCompatActivity {
    private Button button;
    private EditText editText;
    final String publicKey = "FLWPUBK_TEST-22b228a9a67e55b0ae4a5208760d776f-X"; //Get your public key from your account
    final String encryptionKey = "FLWSECK_TEST81b6971aacc5"; //Get your encryption key from your account
    private String country;
    private String currency;
    private String email;
    private String fName;
    private String lName;
    private String narration;
    String txRef;
    private String amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        button = findViewById(R.id.button_donate);
        editText = findViewById(R.id.editText_amount);
        country = "NG";
        currency = "NGN";
        email = "Igboanyika19.com"; // Bsckend code to get email name goes here
        fName = "Nnaemeka"; // Bsckend code to get first name goes here
        lName = "Igboanyika"; // Bsckend code to get last name goes here
        narration = "Donation";
        txRef = email +" "+  UUID.randomUUID().toString();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editText.getText().toString().trim().matches("")){
                    Toast.makeText(getApplicationContext(), "Donation should not be empty", Toast.LENGTH_LONG).show();
                }
                else if(editText.getText().length() < 3 && (!editText.getText().toString().trim().matches("")) ){
                    Toast.makeText(getApplicationContext(), "The minimun donation is 100", Toast.LENGTH_LONG).show();
                }
                else {
                    amount = editText.getText().toString();
                    new RavePayManager(DonateActivity.this).setAmount(Integer.valueOf(amount))
                            .setCountry(country)
                            .setCurrency(currency)
                            .setEmail(email)
                            .setfName(fName)
                            .setlName(lName)
                            .setNarration(narration)
                            .setPublicKey(publicKey)
                            .setEncryptionKey(encryptionKey)
                            .setTxRef(txRef)
                            .acceptAccountPayments(true)
                            .acceptCardPayments(true)
                            .acceptMpesaPayments(false)
                            .onStagingEnv(false)
                            .withTheme(R.style.DefaultTheme)
                            .allowSaveCardFeature(true)
                            .initialize();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        /*
         *  We advise you to do a further verification of transaction's details on your server to be
         *  sure everything checks out before providing service or goods.
         */
        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            String message = data.getStringExtra("response");
            if (resultCode == RavePayActivity.RESULT_SUCCESS) {
                Toast.makeText(this, "SUCCESS " + message, Toast.LENGTH_SHORT).show();
            }
            else if (resultCode == RavePayActivity.RESULT_ERROR) {
                Toast.makeText(this, "ERROR " + message, Toast.LENGTH_SHORT).show();
            }
            else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
                Toast.makeText(this, "CANCELLED " + message, Toast.LENGTH_SHORT).show();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
