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
import com.project.android_kidstories.data.model.Story;
import com.project.android_kidstories.data.source.local.relational.database.StoryLab;
import com.project.android_kidstories.ui.downloads.adapters.DownloadsAdapter;

import java.util.List;

public class DownloadsFragment extends Fragment {

    private DownloadsAdapter exploreAdapter;
    private StoryLab storyLab;
    private RecyclerView recyclerView;
    private View emptyView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_downloads, container, false);

        storyLab = StoryLab.get(requireContext());

        recyclerView = root.findViewById(R.id.saved_stories_recycler);
        emptyView = root.findViewById(R.id.empty_downloads);

        exploreAdapter = new DownloadsAdapter(requireContext());
        recyclerView.setAdapter(exploreAdapter);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();

        List<Story> storyList = storyLab.getStories();
        if (storyList.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        exploreAdapter.submitList(storyLab.getStories());
    }
}
