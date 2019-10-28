package com.project.android_kidstories.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.android_kidstories.Model.Story;
import com.project.android_kidstories.R;

import java.util.List;

/**
 * @author .: Ukeje Emeka
 * @email ..: ukejee3@gmail.com
 * @created : 10/28/19
 */
public class MyStoriesAdapter extends RecyclerView.Adapter<MyStoriesAdapter.MyViewHolder> {

    public List<Story> myStoryList;
    private Context context;
    private View.OnClickListener listener;

    public MyStoriesAdapter(List<Story> list, Context context, View.OnClickListener listener){

        this.myStoryList = list;
        this.context = context;
        this.listener = listener;
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
        holder.likeCount.setText(Integer.toString(story.getLikesCount()));
        holder.dislikeCount.setText(Integer.toString(story.getDislikesCount()));
        Glide.with(context)
                .load(story.getImageUrl())
                .into(holder.storyImage);
        //holder.itemView.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return myStoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView storyImage;
        TextView storyTitle;
        TextView storyDuration;
        TextView storyAuthor;
        TextView likeCount;
        TextView dislikeCount;
        TextView commentCount;

        public MyViewHolder(@NonNull View itemView) {
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
