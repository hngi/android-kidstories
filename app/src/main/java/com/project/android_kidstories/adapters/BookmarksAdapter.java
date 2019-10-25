package com.project.android_kidstories.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.android_kidstories.Api.Api;
import com.project.android_kidstories.Api.RetrofitClient;
import com.project.android_kidstories.Model.Story;
import com.project.android_kidstories.R;
import com.project.android_kidstories.Views.main.MainActivity;
import com.project.android_kidstories.ui.profile.BookmarksFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.ViewHolder> {
    public static String token = "";
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

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDeleteDialog(holder.currentStory.getTitle(), holder.currentStory);


                return true;

            }
        });
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    public interface OnBookmarkClickListener {
        void onStoryClick(int storyId);
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
            author.setText("by " + currentStory.getAuthor());
            likeCount.setText(String.valueOf(currentStory.getLikesCount()));
            dislikeCount.setText(String.valueOf(currentStory.getDislikesCount()));

        }

        @Override
        public void onClick(View v) {
            listener.onStoryClick(currentStory.getId());
        }
    }

    private void showDeleteDialog(String storyName, Story story) {

        androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(
                context);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                removeStory(story);
                RecyclerStoriesAdapter.deleteStory(context,story.getId());

            }
        });
        alertDialog.setNegativeButton("No", null);
        alertDialog.setMessage("Remove " + storyName + " from bookmarks?");
        alertDialog.setTitle(R.string.app_name);
        alertDialog.show();
    }

    private void removeStory(Story currentStory){
        for (Story story : stories){
            if (story == currentStory){
                stories.remove(story);
            }
        }
    }

}
