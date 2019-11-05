package com.project.android_kidstories.ui.home.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.project.android_kidstories.R;
import com.project.android_kidstories.data.model.Story;

import java.util.List;

public class ExploreAdapter extends RecyclerView.Adapter<ExploreAdapter.ViewHolder> {

    private List<Story> stories;

    public ExploreAdapter(List<Story> stories) {
        this.stories = stories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_explore_stories, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Story currentStory = stories.get(position);

        holder.storyTitle.setText(currentStory.getTitle());
        holder.storyAuthor.setText(String.format("by %s", currentStory.getAuthor()));
        // Replace ID with actual category name
        holder.storyCategory.setText(currentStory.getCategoryId());
        if (currentStory.isBookmark()) {
            holder.bookmark.setActivated(true);
        } else {
            holder.bookmark.setActivated(false);
        }
        Glide.with(holder.itemView)
                .load(currentStory.getImageUrl())
                .into(holder.storyImage);

        // Set first 300 characters as description
        holder.storyDescription.setText(currentStory.getBody().substring(0, 300));
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView storyImage;
        private TextView storyTitle;
        private TextView storyAuthor;
        private TextView storyDescription;
        private Chip storyCategory;
        private ImageView bookmark;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            storyImage = itemView.findViewById(R.id.img_itemimage_explore);
            storyTitle = itemView.findViewById(R.id.txt_itemtitle_explore);
            storyAuthor = itemView.findViewById(R.id.txt_itemauthor_explore);
            storyDescription = itemView.findViewById(R.id.txt_itemdesc_explore);
            storyCategory = itemView.findViewById(R.id.chip_itemcategory_explore);
            bookmark = itemView.findViewById(R.id.img_itembookmarked_explore);

            itemView.setOnClickListener(v -> {
                // Navigate to Single Story Activity
            });
        }
    }
}
