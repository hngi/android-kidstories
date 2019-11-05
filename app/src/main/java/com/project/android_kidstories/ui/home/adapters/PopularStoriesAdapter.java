package com.project.android_kidstories.ui.home.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.project.android_kidstories.R;
import com.project.android_kidstories.data.model.Story;

import java.util.List;

public class PopularStoriesAdapter extends RecyclerView.Adapter<PopularStoriesAdapter.ViewHolder> {

    private List<Story> stories;

    public PopularStoriesAdapter(List<Story> stories) {
        this.stories = stories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_popular_stories, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Story currentStory = stories.get(position);

        holder.storyTitle.setText(currentStory.getTitle());
        holder.storyAgeRange.setText(String.format("Ages %s", currentStory.getAge()));
        holder.storyDuration.setText(currentStory.getStoryDuration());
        Glide.with(holder.itemView)
                .load(currentStory.getImageUrl())
                .into(holder.storyImage);
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView storyImage;
        private TextView storyTitle;
        private TextView storyAgeRange;
        private TextView storyDuration;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            storyImage = itemView.findViewById(R.id.img_itemimage_popularstories);
            storyTitle = itemView.findViewById(R.id.txt_itemtitle_popularstories);
            storyAgeRange = itemView.findViewById(R.id.txt_itemage_popularstories);
            storyDuration = itemView.findViewById(R.id.txt_itemduration_popularstories);

            itemView.setOnClickListener(v -> {
                // Navigate to Single Story Activity
            });

        }
    }
}
