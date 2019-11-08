package com.project.android_kidstories.ui.home.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.project.android_kidstories.R;
import com.project.android_kidstories.data.model.Story;
import com.project.android_kidstories.ui.story_viewing.SingleStoryActivity;

import java.util.Objects;


public class ExploreAdapter extends ListAdapter<Story, ExploreAdapter.ViewHolder> {

    private Context context;
    private OnBookmark onBookmarkListener;

    public ExploreAdapter(Fragment fragment) {
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

        try {
            onBookmarkListener = (OnBookmark) fragment;
            this.context = fragment.getContext();

        } catch (IllegalStateException ise) {
            Toast.makeText(context, "Context must implement OnBookmark", Toast.LENGTH_SHORT).show();
            throw ise;
        }
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
            holder.bookmark.setSelected(true);
        } else {
            holder.bookmark.setSelected(false);
        }

        //holder.commentCount.setText(currentStory.getComments().getComments().size());
        holder.likeCount.setText(String.valueOf(currentStory.getLikesCount()));
        holder.dislikeCount.setText(String.valueOf(currentStory.getDislikesCount()));
        holder.ageRange.setText(String.format("ages %s", currentStory.getAge()));
        Glide.with(holder.itemView)
                .load(currentStory.getImageUrl())
                .into(holder.storyImage);

        holder.itemView.setOnClickListener(v -> {
            // Navigate to Single Story Activity
            Intent intent = new Intent(context, SingleStoryActivity.class);
            intent.putExtra(SingleStoryActivity.STORY_ID_KEY, currentStory.getId());
            intent.putExtra(SingleStoryActivity.STORY_NAME_KEY, currentStory.getTitle());
            context.startActivity(intent);
        });

        holder.storyDescription.setText(currentStory.getBody());

        holder.bookmark.setOnClickListener(view -> {
            currentStory.setBookmark(!currentStory.isBookmark());
            // Save
            setBookmark(holder, currentStory);
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_explore_stories, parent, false);
        return new ViewHolder(itemView);
    }

    private void setBookmark(ViewHolder holder, Story story) {
        if (story.isBookmark()) {
            holder.bookmark.setSelected(true);
        } else {
            holder.bookmark.setSelected(false);
        }

        onBookmarkListener.onBookmark(story);
    }

    public interface OnBookmark {
        void onBookmark(Story story);
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
        private ImageView bookmark;
        private TextView commentCount;
        private TextView likeCount;
        private TextView dislikeCount;
        private TextView ageRange;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            storyImage = itemView.findViewById(R.id.img_itemimage_explore);
            storyTitle = itemView.findViewById(R.id.txt_itemtitle_explore);
            storyAuthor = itemView.findViewById(R.id.txt_itemauthor_explore);
            storyDescription = itemView.findViewById(R.id.txt_itemdesc_explore);
            bookmark = itemView.findViewById(R.id.img_itembookmarked_explore);
            commentCount = itemView.findViewById(R.id.txt_itemcommentcount_explore);
            likeCount = itemView.findViewById(R.id.txt_itemlikecount_explore);
            dislikeCount = itemView.findViewById(R.id.txt_itemdislikecount_explore);
            ageRange = itemView.findViewById(R.id.txt_itemage_explore);
        }
    }
}
