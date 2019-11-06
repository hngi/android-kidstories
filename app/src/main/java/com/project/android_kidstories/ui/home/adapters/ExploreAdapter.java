package com.project.android_kidstories.ui.home.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.project.android_kidstories.R;
import com.project.android_kidstories.data.model.Story;

import java.util.Objects;


public class ExploreAdapter extends ListAdapter<Story, ExploreAdapter.ViewHolder> {

    public ExploreAdapter() {
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
        Story currentStory = getCurrentList().get(position);

        Log.d("GLOBAL_SCOPE", "In onBindViewHolder");

        holder.storyTitle.setText(currentStory.getTitle());
        holder.storyAuthor.setText(String.format("by %s", currentStory.getAuthor()));
        // Replace ID with actual category name
        // holder.storyCategory.setText(String.valueOf(currentStory.getCategoryId()));
        if (currentStory.isBookmark()) {
            holder.bookmark.setActivated(true);
        } else {
            holder.bookmark.setActivated(false);
        }
        Glide.with(holder.itemView)
                .load(currentStory.getImageUrl())
                .into(holder.storyImage);

        holder.storyDescription.setText(currentStory.getBody());
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
        // private Chip storyCategory;
        private ImageView bookmark;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            storyImage = itemView.findViewById(R.id.img_itemimage_explore);
            storyTitle = itemView.findViewById(R.id.txt_itemtitle_explore);
            storyAuthor = itemView.findViewById(R.id.txt_itemauthor_explore);
            storyDescription = itemView.findViewById(R.id.txt_itemdesc_explore);
//            storyCategory = itemView.findViewById(R.id.chip_itemcategory_explore);
            bookmark = itemView.findViewById(R.id.img_itembookmarked_explore);

            /*itemView.setOnClickListener(v -> {
                // Navigate to Single Story Activity
            });*/
        }
    }
}
