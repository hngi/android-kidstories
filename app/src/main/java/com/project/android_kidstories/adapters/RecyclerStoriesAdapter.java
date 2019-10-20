package com.project.android_kidstories.adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.Resource;
import com.project.android_kidstories.Api.Responses.story.StoryAllResponse;
import com.project.android_kidstories.R;
import com.project.android_kidstories.SingleStoryActivity;

/**
 * @author .: Oluwajuwon Fawole
 * @created : 16/10/19
 */
public class RecyclerStoriesAdapter extends RecyclerView.Adapter<RecyclerStoriesAdapter.CustomViewHolder>{


    private Context context;
    private StoryAllResponse storiesList;



    public RecyclerStoriesAdapter(Context context, StoryAllResponse storiesList) {
        this.context = context;
        this.storiesList = storiesList;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View view;
        ImageView storyImage;
        TextView storyTitle;
        TextView authorName;
        TextView ageRange;
        TextView num_likes;
        TextView num_dislikes;
        ImageView like;
        ImageView dislike;
        ImageView bookmark;
        LinearLayout list_item;

        CustomViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            storyImage = view.findViewById(R.id.recyclerImage);
            storyTitle = view.findViewById(R.id.recyclerName);
            authorName = view.findViewById(R.id.tv2);
            ageRange = view.findViewById(R.id.tv3);
            num_likes = view.findViewById(R.id.count1);
            num_dislikes = view.findViewById(R.id.count2);
            like = view.findViewById(R.id.img_like);
            dislike = view.findViewById(R.id.img_dislike);
            bookmark = view.findViewById(R.id.bookmark);
            list_item = view.findViewById(R.id.l_clickable);
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Glide.with(context).load(storiesList.getData().get(position).getImageUrl()).into(holder.storyImage);

        holder.storyTitle.setText(storiesList.getData().get(position).getTitle());
        holder.authorName.setText("By "+storiesList.getData().get(position).getAuthor());

        holder.ageRange.setText("For kids ages "+storiesList.getData().get(position).getAge());
        holder.num_likes.setText(String.valueOf(storiesList.getData().get(position).getLikesCount()));
        holder.num_dislikes.setText(String.valueOf(storiesList.getData().get(position).getDislikesCount()));


        holder.list_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int story_id = storiesList.getData().get(position).getId();
                Intent intent = new Intent(context, SingleStoryActivity.class);
                intent.putExtra("story_id", story_id);
                context.startActivity(intent);
            }
        });

        holder.like.setTag(R.drawable.ic_thumb_up_black_24dp);    //When you change the drawable
        holder.dislike.setTag(R.drawable.ic_thumb_down_black_24dp);

        holder.bookmark.setTag(R.drawable.ic_bookmark_border_black_24dp);

        int like_image_black = R.drawable.ic_thumb_up_black_24dp;
        int like_image_blue  = R.drawable.ic_thumb_up_blue_24dp;

        int dislike_image_black = R.drawable.ic_thumb_down_black_24dp;
        int dislike_image_blue  = R.drawable.ic_thumb_down_blue_24dp;

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int like_drawableId = (Integer)holder.like.getTag();
                int dislike_drawableId = (Integer)holder.dislike.getTag();

                if(like_drawableId == like_image_black ||  dislike_drawableId == dislike_image_blue) {
                    holder.like.setImageResource(like_image_blue);
                    holder.like.setTag(like_image_blue);

                    int like_count = Integer.parseInt(holder.num_likes.getText().toString());
                    like_count++;
                    holder.num_likes.setText(String.valueOf(like_count));

                    if(dislike_drawableId == dislike_image_blue){
                        holder.dislike.setImageResource(dislike_image_blue);
                        holder.dislike.setTag(dislike_image_black);

                        int dislike_count = Integer.parseInt(holder.num_dislikes.getText().toString());
                        dislike_count--;
                        holder.num_dislikes.setText(String.valueOf(dislike_count));
                    }
                }else{
                    holder.like.setImageResource(like_image_black);
                    holder.like.setTag(like_image_black);

                    int like_count = Integer.parseInt(holder.num_likes.getText().toString());
                    like_count--;
                    holder.num_likes.setText(String.valueOf(like_count));
                }
            }
        });

        holder.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int dislike_drawableId = (Integer)holder.dislike.getTag();
                int like_drawableId = (Integer)holder.like.getTag();

                if(dislike_drawableId == R.drawable.ic_thumb_down_black_24dp || like_drawableId == R.drawable.ic_thumb_up_blue_24dp) {
                    holder.dislike.setImageResource(dislike_image_blue);
                    holder.dislike.setTag(dislike_image_blue);

                    int dislike_count = Integer.parseInt(holder.num_dislikes.getText().toString());
                    dislike_count++;
                    holder.num_dislikes.setText(String.valueOf(dislike_count));

                    if(like_drawableId == like_image_blue){
                        holder.like.setImageResource(like_image_black);
                        holder.like.setTag(like_image_black);

                        int like_count = Integer.parseInt(holder.num_likes.getText().toString());
                        like_count--;
                        holder.num_likes.setText(String.valueOf(like_count));
                    }

                }else{
                    holder.dislike.setImageResource(dislike_image_black);
                    holder.dislike.setTag(dislike_image_black);

                    int dislike_count = Integer.parseInt(holder.num_dislikes.getText().toString());
                    dislike_count--;
                    holder.num_dislikes.setText(String.valueOf(dislike_count));
                }
            }
        });

        holder.bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int bookmark_drawableId = (Integer)holder.bookmark.getTag();

                if(bookmark_drawableId == R.drawable.ic_bookmark_border_black_24dp) {
                    holder.bookmark.setImageResource(R.drawable.ic_bookmark_click_24dp);
                    holder.bookmark.setTag(R.drawable.ic_bookmark_click_24dp);

                }else{
                    holder.bookmark.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                    holder.bookmark.setTag(R.drawable.ic_bookmark_border_black_24dp);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return storiesList.getData().size();
    }
}
