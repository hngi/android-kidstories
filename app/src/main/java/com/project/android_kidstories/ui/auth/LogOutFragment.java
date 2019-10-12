package com.project.android_kidstories.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class LogOutFragment extends Fragment {

    private LogOutViewModel logOutViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        logOutViewModel = ViewModelProviders.of(this).get(LogOutViewModel.class);
        getActivity().finish();
        return null;
    }
}