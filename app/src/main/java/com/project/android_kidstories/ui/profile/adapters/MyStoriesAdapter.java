package com.project.android_kidstories.ui.profile.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.project.android_kidstories.ui.story_viewing.SingleStoryActivity;

import java.util.List;

/**
 * @author .: Ukeje Emeka
 * @email ..: ukejee3@gmail.com
 * @created : 10/28/19
 */
public class MyStoriesAdapter extends RecyclerView.Adapter<MyStoriesAdapter.MyViewHolder> {

    private List<Story> myStoryList;
    private Context context;

    public MyStoriesAdapter(List<Story> list, Context context) {
        this.myStoryList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_stories_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Story story = myStoryList.get(position);

        holder.storyTitle.setText(story.getTitle());
        holder.storyAuthor.setText(story.getAuthor());
        holder.storyDuration.setText(story.getStoryDuration());
        holder.likeCount.setText(String.valueOf(story.getLikesCount()));
        holder.dislikeCount.setText(String.valueOf(story.getDislikesCount()));

        Glide.with(context)
                .load(story.getImageUrl())
                .into(holder.storyImage);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SingleStoryActivity.class);
            intent.putExtra(SingleStoryActivity.STORY_ID_KEY, story.getId());
            intent.putExtra(SingleStoryActivity.STORY_NAME_KEY, story.getTitle());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return myStoryList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView storyImage;
        TextView storyTitle;
        TextView storyDuration;
        TextView storyAuthor;
        TextView likeCount;
        TextView dislikeCount;
        TextView commentCount;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            storyImage = itemView.findViewById(R.id.story_image);
            storyTitle = itemView.findViewById(R.id.story_title);
            storyDuration = itemView.findViewById(R.id.story_duration);
            storyAuthor = itemView.findViewById(R.id.story_author);
            likeCount = itemView.findViewById(R.id.like_count);
            dislikeCount = itemView.findViewById(R.id.dislike_count);
            commentCount = itemView.findViewById(R.id.comment_count);
        }
    }
}
