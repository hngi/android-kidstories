package com.project.android_kidstories.adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.android_kidstories.Api.Responses.BaseResponse;
import com.project.android_kidstories.Api.Responses.Category.CategoryStoriesResponse;
import com.project.android_kidstories.Api.Responses.story.StoryAllResponse;
import com.project.android_kidstories.Model.Story;
import com.project.android_kidstories.R;
import com.project.android_kidstories.SingleStoryActivity;

import java.util.List;

/**
 * @author .: Joshua Erondu
 * @created : 16/10/19
 */
public class RecyclerCategoryStoriesAdapter extends RecyclerView.Adapter<RecyclerCategoryStoriesAdapter.CustomViewHolder>{


    private Context context;
    private List<Story> storiesList;



    public RecyclerCategoryStoriesAdapter(Context context, List<Story> storiesList) {
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
        CardView list_item;


        CustomViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            storyImage = view.findViewById(R.id.book_image);
            storyTitle = view.findViewById(R.id.book_title);
            authorName = view.findViewById(R.id.author_name);
            num_likes = view.findViewById(R.id.like_count);
            num_dislikes = view.findViewById(R.id.dislike_count);
            like = view.findViewById(R.id.like_button);
            dislike = view.findViewById(R.id.dislike_button);
            bookmark = view.findViewById(R.id.bookmark_button);
            list_item = view.findViewById(R.id.cardView);
            ageRange = view.findViewById(R.id.book_description);

        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.story_listing_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        Glide.with(context).load(storiesList.get(position).getImageUrl()).into(holder.storyImage);

        holder.storyTitle.setText(storiesList.get(position).getTitle());
        holder.authorName.setText(storiesList.get(position).getAuthor());

        holder.num_likes.setText(String.valueOf(storiesList.get(position).getLikesCount()));
        holder.num_dislikes.setText(String.valueOf(storiesList.get(position).getDislikesCount()));

        holder.ageRange.setText("For kids aged " + storiesList.get(position).getAge());


        holder.list_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int story_id = storiesList.get(position).getId();
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
                        holder.dislike.setImageResource(dislike_image_black);
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
        return storiesList.size();
    }
}
