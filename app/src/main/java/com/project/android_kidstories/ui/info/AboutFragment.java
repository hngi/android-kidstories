package com.project.android_kidstories.ui.info;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.project.android_kidstories.R;
import com.project.android_kidstories.ui.base.BaseFragment;

public class AboutFragment extends BaseFragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about, container, false);
        // Update Activity's toolbar title
        try {
            ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("About");
        } catch (NullPointerException npe) {
            Log.d("GLOBAL_SCOPE", "Can't set toolbar title");
        }

        return root;
    }
}