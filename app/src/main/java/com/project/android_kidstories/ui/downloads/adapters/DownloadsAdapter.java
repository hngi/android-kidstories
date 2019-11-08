package com.project.android_kidstories.ui.downloads.adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.project.android_kidstories.R;
import com.project.android_kidstories.data.model.Story;
import com.project.android_kidstories.ui.story_viewing.SingleStoryActivity;

import java.util.Objects;

import static com.project.android_kidstories.utils.CommonUtils.loadBitmap;

public class DownloadsAdapter extends ListAdapter<Story, DownloadsAdapter.ViewHolder> {

    private Fragment fragment;

    public DownloadsAdapter(Fragment fragment) {
        super(new DiffUtil.ItemCallback<Story>() {
            @Override
            public boolean areItemsTheSame(@NonNull Story oldItem, @NonNull Story newItem) {
                return oldItem == newItem;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Story oldItem, @NonNull Story newItem) {
                return Objects.equals(oldItem.getId(), newItem.getId());
            }
        });

        this.fragment = fragment;
        try {
            OnStoryDelete on = (OnStoryDelete) fragment;
        } catch (ClassCastException cce) {
            Log.d("GLOBAL_TAG", "fragment must implement OnStoryDelete");
            throw cce;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Story currentStory = getCurrentList().get(position);

        Log.d("GLOBAL_SCOPE", "In onBindViewHolder");

        holder.storyTitle.setText(currentStory.getTitle());
        holder.storyAuthor.setText(String.format("by %s", currentStory.getAuthor()));

        Bitmap bitmap = loadBitmap(fragment.requireContext(), currentStory.getTitle() + ".png");
        holder.storyImage.setImageBitmap(bitmap);

        holder.storyDescription.setText(currentStory.getBody());

        holder.itemView.setOnClickListener(v -> {
            // Navigate to Single Story Activity
            Intent intent = new Intent(fragment.requireContext(), SingleStoryActivity.class);
            intent.putExtra(SingleStoryActivity.STORY_NAME_KEY, currentStory.getTitle());
            intent.putExtra(SingleStoryActivity.STORY_ID_KEY, currentStory.getId());
            fragment.startActivity(intent);
        });

        holder.remove.setOnClickListener(v -> {
            ((OnStoryDelete) fragment).onStoryDelete(currentStory);
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_download_stories, parent, false);
        return new ViewHolder(itemView);
    }

    public interface OnStoryDelete {
        void onStoryDelete(Story story);
    }

    @Override
    public int getItemCount() {
        return getCurrentList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView storyImage;
        private TextView storyTitle;
        private TextView storyAuthor;
        private TextView storyDescription;
        private ImageView remove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            storyImage = itemView.findViewById(R.id.img_itemimage_downloads);
            storyTitle = itemView.findViewById(R.id.txt_itemtitle_downloads);
            storyAuthor = itemView.findViewById(R.id.txt_itemauthor_downloads);
            storyDescription = itemView.findViewById(R.id.txt_itemdesc_downloads);
            remove = itemView.findViewById(R.id.img_itemremove_downloads);
        }
    }
}
