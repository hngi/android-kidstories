package com.project.android_kidstories.ui.support;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.flutterwave.raveandroid.RaveConstants;
import com.flutterwave.raveandroid.RavePayActivity;
import com.flutterwave.raveandroid.RavePayManager;
import com.project.android_kidstories.R;

import java.util.UUID;

import static com.facebook.FacebookSdk.getApplicationContext;

public class DonateFragment extends Fragment {
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

    private DonateViewModel donateViewModel;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        /*
         *  We advise you to do a further verification of transaction's details on your server to be
         *  sure everything checks out before providing service or goods.
         */
        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
            String message = data.getStringExtra("response");
            if (resultCode == RavePayActivity.RESULT_SUCCESS) {
                Toast.makeText(getApplicationContext(), "SUCCESS " + message, Toast.LENGTH_SHORT).show();
            }
            else if (resultCode == RavePayActivity.RESULT_ERROR) {
                Toast.makeText(getApplicationContext(), "ERROR " + message, Toast.LENGTH_SHORT).show();
            }
            else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
                Toast.makeText(getApplicationContext(), "CANCELLED " + message, Toast.LENGTH_SHORT).show();
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        donateViewModel = ViewModelProviders.of(this).get(DonateViewModel.class);
        View root = inflater.inflate(R.layout.fragment_donate, container, false);

        button = root.findViewById(R.id.button_donate);
        editText = root.findViewById(R.id.editText_amount);
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

                if (editText.getText().toString().trim().matches("")) {
                    Toast.makeText(getApplicationContext(), "Donation should not be empty", Toast.LENGTH_LONG).show();
                } else if (editText.getText().length() < 3 && (!editText.getText().toString().trim().matches(""))) {
                    Toast.makeText(getApplicationContext(), "The minimun donation is 100", Toast.LENGTH_LONG).show();
                } else {
                    amount = editText.getText().toString();
                    new RavePayManager(DonateFragment.this.getActivity()).setAmount(Integer.valueOf(amount))
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

        return root;
    }

}