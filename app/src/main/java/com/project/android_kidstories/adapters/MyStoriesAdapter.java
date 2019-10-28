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

/**
 * @author .: Ukeje Emeka
 * @email ..: ukejee3@gmail.com
 * @created : 10/28/19
 */
public class MyStoriesAdapter extends RecyclerView.Adapter<MyStoriesAdapter.MyViewHolder> {

    public List<Story> myStoryList;

    public MyStoriesAdapter(List<Story> list){
        this.myStoryList = list;
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
        holder.storyDescription.setText(story.getCategoryId());
    }

    @Override
    public int getItemCount() {
        return myStoryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView storyImage;
        TextView storyTitle, storyDescription, storyAuthor;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            storyImage = itemView.findViewById(R.id.my_story_image);
            storyTitle = itemView.findViewById(R.id.my_story_title);
            storyDescription = itemView.findViewById(R.id.my_story_description);
            storyAuthor = itemView.findViewById(R.id.my_story_author);
        }
    }
}
