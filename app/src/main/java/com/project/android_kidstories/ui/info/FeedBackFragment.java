package com.project.android_kidstories.ui.info;

import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.firebase.client.Firebase;
import com.project.android_kidstories.R;

import static com.facebook.FacebookSdk.getApplicationContext;

public class FeedBackFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_feed_back, container, false);

        TextView name = root.findViewById(R.id.fed_Name);
        TextView email = root.findViewById(R.id.fed_Email);
        TextView message = root.findViewById(R.id.fed_Message);

        String theEmail = email.getText().toString().trim();
        String theName = name.getText().toString().trim();
        String theMessage = message.getText().toString().trim();


        Firebase.setAndroidContext(requireActivity());
        String UniqueID = Settings.Secure.getString(getApplicationContext()
                .getContentResolver(), Settings.Secure.ANDROID_ID);
        Firebase firebase = new Firebase("https://androidkidstories.firebaseio.com/Users" + UniqueID);


        // SendFeedBack Button
        Button sendFeedback = root.findViewById(R.id.fed_send_feed_back);
        sendFeedback.setOnClickListener(v -> {
            if (theName.isEmpty()) {
                name.setError("This is required");
            } else if (theEmail.isEmpty()) {
                email.setError("This is required");
            } else if (theMessage.isEmpty()) {
                message.setError("This is required");
            } else {
                name.setError(null);
                email.setError(null);
                message.setError(null);

                // For Email
                Firebase child_email = firebase.child("Email");
                child_email.setValue(theEmail);

                // For Message
                Firebase child_message = firebase.child("Message");
                child_message.setValue(theMessage);

                // For Name
                Firebase child_name = firebase.child("Name");
                child_name.setValue(theName);

                Toast.makeText(requireContext(), "Feedback sent", Toast.LENGTH_SHORT).show();
                email.setText("");
                name.setText("");
                message.setText("");
            }

        });

        return root;
    }
}
