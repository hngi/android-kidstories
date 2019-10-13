package com.project.android_kidstories.Views.main.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.facebook.login.LoginManager;
import com.project.android_kidstories.Views.RegisterActivity;


public class LogOutFragment extends Fragment {

    private com.project.android_kidstories.ui.auth.LogOutViewModel logOutViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        logOutViewModel = ViewModelProviders.of(this).get(com.project.android_kidstories.ui.auth.LogOutViewModel.class);
        // Logout of facebook
        if (LoginManager.getInstance() != null) {
            LoginManager.getInstance().logOut();
        }
        Intent intent = new Intent(getActivity(), RegisterActivity.class);
        startActivity(intent);
        getActivity().finish();
        return null;
    }
}