package com.project.android_kidstories.ui.home;

import android.os.Bundle;
import android.view.*;
import androidx.annotation.NonNull;
import com.project.android_kidstories.R;
import com.project.android_kidstories.ui.base.BaseFragment;

public class HomeFragment extends BaseFragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // This fragment needs to show a menu
        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}