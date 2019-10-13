package com.project.android_kidstories.Views.main.ui.home;

import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

public class BaseFragment extends Fragment {


    //TODO: Ehma Refactor to BaseFragment
    public void showToast(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    public void showSnackBar(View view, String msg){
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

}
