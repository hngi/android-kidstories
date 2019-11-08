package com.project.android_kidstories.ui.profile.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.project.android_kidstories.R;
import com.project.android_kidstories.adapters.RecyclerStoriesAdapter;
import com.project.android_kidstories.data.model.Story;
import com.project.android_kidstories.data.source.remote.api.Api;

import java.util.List;


public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.ViewHolder> {

    private final Context context;
    private List<Story> stories;
    private Api service;

    private OnBookmarkClickListener listener;

    public BookmarksAdapter(List<Story> stories, OnBookmarkClickListener listener, Context context) {
        this.stories = stories;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bookmark, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);

        holder.itemView.setOnLongClickListener(v -> {
            showDeleteDialog(holder.currentStory.getTitle(), holder.currentStory);

            return true;

        });
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    public interface OnBookmarkClickListener {
        void onStoryClick(int storyId, String storyName);
    }

    private void showDeleteDialog(String storyName, Story story) {

        androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(
                context);

        alertDialog.setPositiveButton("Yes", (dialog, which) -> {
            removeStory(story);
            RecyclerStoriesAdapter.deleteStory(context, story.getId());

        });
        alertDialog.setNegativeButton("No", null);
        alertDialog.setMessage("Remove " + storyName + " from bookmarks?");
        alertDialog.setTitle(R.string.app_name);
        alertDialog.show();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView image;
        private TextView title;
        private TextView author;
        private ImageView likeIcon;
        private TextView likeCount;
        private ImageView disikeIcon;
        private TextView dislikeCount;
        private Story currentStory;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.bkmrk_img);
            title = itemView.findViewById(R.id.bookmark_name);
            author = itemView.findViewById(R.id.bookmark_author);
            likeIcon = itemView.findViewById(R.id.bkmrk_like);
            likeCount = itemView.findViewById(R.id.bkmrkcount1);
            disikeIcon = itemView.findViewById(R.id.bkmrk_dislike);
            dislikeCount = itemView.findViewById(R.id.bkmrkcount2);

            itemView.setOnClickListener(this);
        }

        void bind(int position) {
            currentStory = stories.get(position);
            title.setText(currentStory.getTitle());
            author.setText(String.format("by %s", currentStory.getAuthor()));
            likeCount.setText(String.valueOf(currentStory.getLikesCount()));
            dislikeCount.setText(String.valueOf(currentStory.getDislikesCount()));
            Glide.with(context).load(currentStory.getImageUrl()).into(image);
        }

        @Override
        public void onClick(View v) {
            listener.onStoryClick(currentStory.getId(), currentStory.getTitle());
        }
    }

    private void removeStory(Story currentStory){
        List<Story> stories1 = stories;
        Log.d("XXX first list", String.valueOf(stories1));
        for (Story story : stories1){
            if (story == currentStory){
                stories1.remove(story);
                Log.d("XXX new list", String.valueOf(stories1));
                stories = stories1;

            }
        }

    }

}
