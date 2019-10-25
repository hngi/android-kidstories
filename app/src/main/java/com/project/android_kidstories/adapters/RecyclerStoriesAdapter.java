package com.project.android_kidstories.adapters;


import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.project.android_kidstories.Api.Api;
import com.project.android_kidstories.Api.Responses.bookmark.BookmarkResponse;
import com.project.android_kidstories.Api.Responses.story.Reaction.ReactionResponse;
import com.project.android_kidstories.Api.Responses.story.StoryAllResponse;
import com.project.android_kidstories.Api.RetrofitClient;
import com.project.android_kidstories.DataStore.Repository;
import com.project.android_kidstories.Model.Story;
import com.project.android_kidstories.R;
import com.project.android_kidstories.SingleStoryActivity;
import com.project.android_kidstories.sharePref.SharePref;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author .: Oluwajuwon Fawole
 * @created : 16/10/19
 */
public class RecyclerStoriesAdapter extends RecyclerView.Adapter<RecyclerStoriesAdapter.CustomViewHolder> {


    public static String token = "";
    private Context context;
    private StoryAllResponse storiesList;
    private OnBookmarked bookmarked;
    private Api service;
    Api storyApi;
    List<Story> stories;

    public RecyclerStoriesAdapter(Context context, StoryAllResponse storiesList, OnBookmarked bookmarked, Repository repository) {
        this.context = context;
        this.storiesList = storiesList;
        this.bookmarked = bookmarked;
        this.storyApi = repository.getStoryApi();
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recycler_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        stories = storiesList.getData();
        Glide.with(context).load(storiesList.getData().get(position).getImageUrl()).into(holder.storyImage);

        holder.storyTitle.setText(storiesList.getData().get(position).getTitle());
        holder.authorName.setText("By " + storiesList.getData().get(position).getAuthor());

        holder.ageRange.setText("For kids ages " + storiesList.getData().get(position).getAge());
        holder.num_likes.setText(String.valueOf(storiesList.getData().get(position).getLikesCount()));
        holder.num_dislikes.setText(String.valueOf(storiesList.getData().get(position).getDislikesCount()));

        int storyId = storiesList.getData().get(position).getId();

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

        Story story = stories.get(position);

        boolean isBookmarked = bookmarked.isAlreadyBookmarked(story.getId(), position) == story.getId();
        Log.e("STORYYyyyyyyyyyy", isBookmarked + "");
        if (isBookmarked) {
            holder.bookmark.setTag(R.drawable.ic_bookmark_click_24dp);
            holder.bookmark.setImageResource(R.drawable.ic_bookmark_click_24dp);
        } else {

            holder.bookmark.setTag(R.drawable.ic_bookmark_border_black_24dp);
        }


        //toggleReaction
        if(story.getReaction().equals("1")){
            holder.like.setImageResource(R.drawable.ic_thumb_up_blue_24dp);
            holder.dislike.setImageResource(R.drawable.ic_thumb_down_black_24dp);
        }
        else if(story.getReaction().equals("0")){
            holder.like.setImageResource(R.drawable.ic_thumb_up_black_24dp);
            holder.dislike.setImageResource(R.drawable.ic_thumb_down_blue_24dp);
        }
        else{
            holder.like.setImageResource(R.drawable.ic_thumb_up_black_24dp);
            holder.dislike.setImageResource(R.drawable.ic_thumb_down_black_24dp);
        }


        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Story story = stories.get(position);
                toggleReaction(1,story,holder);
                likeStory(story, story.getId(),
                        holder);

            }
        });
        holder.dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Story story = stories.get(position);
                toggleReaction(0,story,holder);
                dislikeStory(story, story.getId());

            }
        });

        // ClickListener for the share Icon
        holder.shareIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = storiesList.getData().get(position).getTitle();
                String body = storiesList.getData().get(position).getBody();
                Intent intent = new Intent(Intent.ACTION_SEND);
                // share only 120 characters if body is longer than or equal to 120
                if (body.length() >= 120) {
                    intent.putExtra(Intent.EXTRA_TEXT, "KIDS STORIES APP \n"
                            + "Story Title: " + title + "\n"
                            + body.substring(0, 120) + "...\n"
                            + "#KidsStories #HNG");
                } else {
                    // share all body characters if body is less than 120
                    intent.putExtra(Intent.EXTRA_TEXT, "KIDS STORIES APP \n"
                            + "Story Title: " + title + "\n"
                            + body + "\n"
                            + "#KidsStories #HNG");
                }
                intent.setType("text/plain");
                context.startActivity(Intent.createChooser(intent, "Send to"));
            }
        });

        holder.bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int bookmark_drawableId = (Integer) holder.bookmark.getTag();

                if ((bookmark_drawableId == R.drawable.ic_bookmark_border_black_24dp)) {
                    bookmarked.onBookmarkAdded(storiesList.getData()
                            .get(position).getId());
                    holder.bookmark.setImageResource(R.drawable.ic_bookmark_click_24dp);
                    holder.bookmark.setTag(R.drawable.ic_bookmark_click_24dp);

                }

                else {
                    service = RetrofitClient.getInstance().create(Api.class);
                    Call<Void> deleteBookmarkedStory = service.deleteBookmarkedStory(token, storyId);
                    deleteBookmarkedStory.enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {

                            if (response.isSuccessful()) Toast.makeText(context, "bookmark removed", Toast.LENGTH_LONG).show();

                            else Toast.makeText(context, "Could not remove bookmark", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {

                        }
                    });


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

    public void likeStory(Story story ,int id ,CustomViewHolder holder){
        //Story will be used for local like
        //id will be used for remote like
        //local like



        //remote like
        String token =  "Bearer " + new SharePref(context).getMyToken();
        storyApi.likeStory(token,id).enqueue(new Callback<ReactionResponse>() {
            @Override
            public void onResponse(Call<ReactionResponse> call, Response<ReactionResponse> response) {
                if (response.isSuccessful()){
                    Log.i("LIKEEE" ,response.body().getMessage() + "\n "+
                            response.body().getStatus()+"\n "+
                            response.body().getAction()+"\n "+
                            response.body().getLikesCount()+"\n "+
                            response.body().getDislikesCount());
                    return;
                }
                Log.i("LIKEEE" ,response.code()+" ");
                return;
            }

            @Override
            public void onFailure(Call<ReactionResponse> call, Throwable t) {
                Log.i("LIKEEE FAILED " ,t.getMessage());
            }
        });
    }

    public void dislikeStory(Story story ,int id){

        //Story will be used for local dislike
        //id will be used for remote dislike


        String token =  "Bearer " + new SharePref(context).getMyToken();
        storyApi.dislikeStory(token,id).enqueue(new Callback<ReactionResponse>() {
            @Override
            public void onResponse(Call<ReactionResponse> call, Response<ReactionResponse> response) {
                if (response.isSuccessful()){
                    Log.i("DISLIKED" ,response.body().getMessage() + "\n "+
                            response.body().getStatus()+"\n "+
                            response.body().getAction()+"\n "+
                            response.body().getLikesCount()+"\n "+
                            response.body().getDislikesCount());
                    return;
                }
                Log.i("RESPONSE NOT DISLIKED" ,response.code()+" ");
                return;
            }

            @Override
            public void onFailure(Call<ReactionResponse> call, Throwable t) {
                Log.i("LIKEEE FAILED " ,t.getMessage());
            }
        });
    }

    public void  toggleReaction(int reaction, Story story, CustomViewHolder holder){
        /**
         * @author .: Erondu Joshua Emeka
         * @created : 25/10/19
         */
        //reaction: 1= like, 0 = dislike;
        if(reaction==1){
            //if like was pressed
            if(story.getReaction().equals("1")){
                story.setReaction("Nil");
                holder.like.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                holder.dislike.setImageResource(R.drawable.ic_thumb_down_black_24dp);
                story.setLikesCount(story.getLikesCount()-1);
                holder.num_likes.setText(String.valueOf(story.getLikesCount()));
            }
            else if(story.getReaction().equals("0")){
                story.setReaction("1");
                holder.like.setImageResource(R.drawable.ic_thumb_up_blue_24dp);
                holder.dislike.setImageResource(R.drawable.ic_thumb_down_black_24dp);
                story.setLikesCount(story.getLikesCount()+1);
                story.setDislikesCount(story.getDislikesCount()-1);
                holder.num_likes.setText(String.valueOf(story.getLikesCount()));
                holder.num_dislikes.setText(String.valueOf(story.getDislikesCount()));


            }
            else{//nil
                story.setReaction("1");
                holder.like.setImageResource(R.drawable.ic_thumb_up_blue_24dp);
                holder.dislike.setImageResource(R.drawable.ic_thumb_down_black_24dp);
                story.setLikesCount(story.getLikesCount()+1);
                holder.num_likes.setText(String.valueOf(story.getLikesCount()));

            }


        }
        else if(reaction==0){
            //if dislike was pressed
            if(story.getReaction().equals("1")){
                story.setReaction("0");
                holder.like.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                holder.dislike.setImageResource(R.drawable.ic_thumb_down_blue_24dp);
                story.setLikesCount(story.getLikesCount()-1);
                story.setDislikesCount(story.getDislikesCount()+1);
                holder.num_likes.setText(String.valueOf(story.getLikesCount()));
                holder.num_dislikes.setText(String.valueOf(story.getDislikesCount()));

            }
            else if(story.getReaction().equals("0")){
                story.setReaction("Nil");
                holder.like.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                holder.dislike.setImageResource(R.drawable.ic_thumb_down_black_24dp);
                story.setDislikesCount(story.getDislikesCount()-1);
                holder.num_dislikes.setText(String.valueOf(story.getDislikesCount()));


            }
            else{
                story.setReaction("0");
                holder.like.setImageResource(R.drawable.ic_thumb_up_black_24dp);
                holder.dislike.setImageResource(R.drawable.ic_thumb_down_blue_24dp);
                story.setDislikesCount(story.getDislikesCount()+1);
                holder.num_dislikes.setText(String.valueOf(story.getDislikesCount()));
            }

        }
        else return;
    }


    //View Holder Class
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
        ImageView shareIcon;
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
            shareIcon = view.findViewById(R.id.share_icon);
            bookmark = view.findViewById(R.id.bookmark);
            list_item = view.findViewById(R.id.l_clickable);
        }
    }

    public interface OnBookmarked {
        boolean onBookmarkAdded(int storyId);

        int isAlreadyBookmarked(int storyId, int pos);
    }


}
