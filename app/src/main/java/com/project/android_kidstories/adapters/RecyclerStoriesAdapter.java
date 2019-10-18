package com.project.android_kidstories.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.android_kidstories.Api.Responses.story.StoryAllResponse;
import com.project.android_kidstories.Model.Story;
import com.project.android_kidstories.R;
import com.project.android_kidstories.SingleStoryActivity;

import java.util.List;

/**
 * @author .: Oluwajuwon Fawole
 * @created : 16/10/19
 */
public class RecyclerStoriesAdapter extends RecyclerView.Adapter<RecyclerStoriesAdapter.CustomViewHolder>{


    private Context context;
    private List<Story> storiesList;
    StoryOnclickListener mListener;

    public interface  StoryOnclickListener{
        void onClick(int position);
    }



    public RecyclerStoriesAdapter(Context context, List<Story> storiesList) {
        this.context = context;
        this.storiesList = storiesList;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View view;
        ImageView storyImage;
        TextView storyTitle;
        TextView authorName;
        TextView ageRange;
        TextView likes;
        TextView dislikes;


        CustomViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            storyImage = view.findViewById(R.id.recyclerImage);
            storyTitle = view.findViewById(R.id.recyclerName);
            authorName = view.findViewById(R.id.tv2);
            ageRange = view.findViewById(R.id.tv3);
            likes = view.findViewById(R.id.count1);
            dislikes = view.findViewById(R.id.count2);

            storyImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Story story = storiesList.get(getAdapterPosition());
                    Intent intent = new Intent(context, SingleStoryActivity.class);
                    intent.putExtra("story", story);
                    context.startActivity(intent);
                }
            });

        }

//        @Override
//        public void onClick(View view) {
//            Intent intent = new Intent(context, SingleStoryActivity.class);
//            intent.putExtra(SingleStoryActivity.STORY_POSITION,getItemCount());
//            context.startActivity(intent);
//        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Glide.with(context).load(storiesList.get(position).getImageUrl()).into(holder.storyImage);

        holder.storyTitle.setText(storiesList.get(position).getTitle());
        holder.authorName.setText(storiesList.get(position).getAuthor());

        holder.ageRange.setText("For kids ages "+storiesList.get(position).getAge());
        holder.likes.setText(storiesList.get(position).getLikesCount()+"");
        holder.dislikes.setText(storiesList.get(position).getDislikesCount()+"");


    }

    @Override
    public int getItemCount() {
        return storiesList.size();
    }


}
