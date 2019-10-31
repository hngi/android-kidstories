package com.project.android_kidstories.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.android_kidstories.Model.Story;
import com.project.android_kidstories.R;
import com.project.android_kidstories.SingleSavedStoryActivity;
import com.project.android_kidstories.SingleStoryActivity;
import com.project.android_kidstories.database.StoryLab;

import java.util.List;

public class SavedStoriesAdapter extends RecyclerView.Adapter<SavedStoriesAdapter.CustomViewHolder> {


    public static final String KEY_STORY_ID= "storyId";
    public static final String KEY_STORY_TITLE= "storyTitle";
    private Context context;
    private List<Story> storiesList;



    public SavedStoriesAdapter(Context context, List<Story> storiesList) {
        this.context = context;
        this.storiesList = storiesList;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.saved_story_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        // Glide.with(context).load(storiesList.get(position).getImageUrl()).into(holder.storyImage);
        holder.storyTitle.setText(storiesList.get(position).getTitle());
        holder.authorName.setText(storiesList.get(position).getAuthor());
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (context, SingleSavedStoryActivity.class);
                // intent.putExtra(KEY_STORY_ID,storiesList.get(position).getId());
                intent.putExtra(KEY_STORY_TITLE,storiesList.get(position).getTitle());
                context.startActivity(intent);
            }
        });
        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View view;
        ImageView storyImage;
        TextView storyTitle;
        TextView authorName;




        CustomViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            //storyImage = view.findViewById(R.id.my_story_image);
            storyTitle = view.findViewById(R.id.saved_story_title);
            authorName = view.findViewById(R.id.author);


        }
    }

    @Override
    public int getItemCount() {
        return storiesList.size();
    }
    public void reloadStories(){
        storiesList= StoryLab.get(context).getStories();
        notifyDataSetChanged();
    }
}
