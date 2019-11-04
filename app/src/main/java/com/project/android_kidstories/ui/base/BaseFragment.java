package com.project.android_kidstories.ui.base;

import android.content.Context;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.material.snackbar.Snackbar;
import com.project.android_kidstories.data.source.local.preferences.SharePref;

public class BaseFragment extends Fragment {

    private SharePref sharePref;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        sharePref = SharePref.getInstance(requireActivity().getApplication());
    }

    public SharePref getSharePref() {
        return sharePref;
    }

    protected void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
    }

    protected void showSnack(String message) {
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show();
    }
}
