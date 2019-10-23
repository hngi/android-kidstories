package com.project.android_kidstories.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.android_kidstories.Model.Story;
import com.project.android_kidstories.R;

import java.util.List;


public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.ViewHolder> {
    private List<Story> stories;

    private OnBookmarkClickListener listener;

    public interface OnBookmarkClickListener {
        void onStoryClick(int storyId);
    }

    public BookmarksAdapter(List<Story> stories,OnBookmarkClickListener listener) {
        this.stories=stories;
        this.listener = listener;
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
    }

    @Override
    public int getItemCount() {
        return stories.size();
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

        void bind(int position){
            currentStory = stories.get(position);
            title.setText(currentStory.getTitle());
            author.setText("by "+currentStory.getAuthor());
            likeCount.setText(String.valueOf(currentStory.getLikesCount()));
            dislikeCount.setText(String.valueOf(currentStory.getDislikesCount()));

        }

        @Override
        public void onClick(View v) {
            listener.onStoryClick(currentStory.getId());
        }
    }
}
