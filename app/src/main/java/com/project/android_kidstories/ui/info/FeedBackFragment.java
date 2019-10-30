package com.project.android_kidstories.ui.info;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.project.android_kidstories.R;
import com.project.android_kidstories.ui.support.DonateViewModel;

import static com.facebook.FacebookSdk.getApplicationContext;

public class FeedBackFragment extends Fragment {

    private Button SendFeedBack;
    private EditText Name, Email, Message;

    Firebase firebase;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_feed_back, container, false);

        Name = root.findViewById(R.id.fed_Name);
        Email = root.findViewById(R.id.fed_Email);
        Message = root.findViewById(R.id.fed_Message);

        SendFeedBack = root.findViewById(R.id.fed_send_feed_back);
        Firebase.setAndroidContext(this.getActivity());

        String UniqueID = Settings.Secure.getString(getApplicationContext()
        .getContentResolver(), Settings.Secure.ANDROID_ID);

        firebase = new Firebase("https://androidkidstories.firebaseio.com/Users" + UniqueID);


        // SendFeedBack Button
        SendFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               final String theEmail = Email.getText().toString();
               final String theName = Name.getText().toString();
               final String theMessage = Message.getText().toString();


                if (theName.isEmpty()){
                    Name.setError("This is required");

                }
                if (theMessage.isEmpty()){
                    Message.setError("This is required");

                }

                if (theEmail.isEmpty()){
                    Email.setError("This is required");

                }
                else {
                    Name.setError(null);

                    Email.setError(null);

                    Message.setError(null);


                    // For Email
                    Firebase child_email = firebase.child("Email");
                    child_email.setValue(theEmail);

                    // For Message
                    Firebase child_message = firebase.child("Message");
                    child_message.setValue(theMessage);

                    // For Name
                    Firebase child_name = firebase.child("Name");
                    child_name.setValue(theName);

                    Toast.makeText(getActivity(), "Feedback sent", Toast.LENGTH_SHORT).show();
                    Email.getText().clear();
                    Name.getText().clear();
                    Message.getText().clear();
                }

            }
        });

        return root;

    }
}
