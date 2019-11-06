package com.project.android_kidstories.ui.downloads;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import com.project.android_kidstories.R;
import com.project.android_kidstories.data.source.local.relational.database.StoryLab;
import com.project.android_kidstories.ui.downloads.adapters.DownloadsAdapter;

public class DownloadsFragment extends Fragment {

    private DownloadsAdapter exploreAdapter;
    private StoryLab storyLab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_saved_stories, container, false);

        storyLab = StoryLab.get(requireContext());

        RecyclerView recyclerView = root.findViewById(R.id.saved_stories_recycler);

        exploreAdapter = new DownloadsAdapter(requireContext());
        recyclerView.setAdapter(exploreAdapter);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        exploreAdapter.submitList(storyLab.getStories());
    }
}
