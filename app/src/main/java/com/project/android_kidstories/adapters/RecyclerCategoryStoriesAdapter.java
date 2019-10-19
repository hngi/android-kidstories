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
        TextView likes;
        TextView dislikes;
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
            likes = view.findViewById(R.id.like_count);
            dislikes = view.findViewById(R.id.dislike_count);
            like = view.findViewById(R.id.like_button);
            dislike = view.findViewById(R.id.dislike_button);
            bookmark = view.findViewById(R.id.bookmark_button);
            list_item = view.findViewById(R.id.cardView);

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
        holder.authorName.setText("By "+storiesList.get(position).getAuthor());

        holder.likes.setText(storiesList.get(position).getLikesCount()+"");
        holder.dislikes.setText(storiesList.get(position).getDislikesCount()+"");


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

        holder.like.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int like_drawableId = (Integer)holder.like.getTag();
                int dislike_drawableId = (Integer)holder.dislike.getTag();

                if(dislike_drawableId == R.drawable.ic_thumb_down_black_24dp || like_drawableId == R.drawable.ic_thumb_up_blue_24dp) {
                    holder.dislike.setImageResource(R.drawable.ic_thumb_down_blue_24dp);
                    holder.dislike.setTag(R.drawable.ic_thumb_down_blue_24dp);

                    holder.like.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                    holder.like.setTag(R.drawable.ic_thumb_up_black_24dp);

                    int count = Integer.parseInt(holder.dislikes.getText().toString());
                    count++;
                    holder.dislikes.setText(""+count);
                }else{
                    holder.dislike.setImageResource(R.drawable.ic_thumb_down_black_24dp);
                    holder.dislike.setTag(R.drawable.ic_thumb_down_black_24dp);

                    int count = Integer.parseInt(holder.dislikes.getText().toString());
                    count--;
                    holder.dislikes.setText(""+count);
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
