package com.project.android_kidstories.adapters;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.pixplicity.easyprefs.library.Prefs;
import com.project.android_kidstories.Api.Api;
import com.project.android_kidstories.Api.Responses.story.Reaction.ReactionResponse;
import com.project.android_kidstories.Api.Responses.story.StoryAllResponse;
import com.project.android_kidstories.Api.RetrofitClient;
import com.project.android_kidstories.DataStore.Repository;
import com.project.android_kidstories.Model.Story;
import com.project.android_kidstories.R;
import com.project.android_kidstories.SingleStoryActivity;
import com.project.android_kidstories.Utils.Common;
import com.project.android_kidstories.sharePref.SharePref;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

/**
 * @author .: Oluwajuwon Fawole
 * @created : 16/10/19
 */
public class RecyclerStoriesAdapter extends RecyclerView.Adapter<RecyclerStoriesAdapter.CustomViewHolder> implements Filterable {


    public static String token = "";
    private Context context;
    private StoryAllResponse storiesList;
    private OnBookmarked bookmarked;
    private Api service;
    Api storyApi;
    List<Story> stories;
    private boolean isLoaderVisible = false;

    public RecyclerStoriesAdapter(Context context, List<Story> storiesList, OnBookmarked bookmarked, Repository repository) {
        this.context = context;
        this.stories =storiesList;
        Collections.reverse(stories);
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
        stories = stories;
        Glide.with(context).load(stories.get(position).getImageUrl()).into(holder.storyImage);

        holder.storyTitle.setText(stories.get(position).getTitle());
        holder.authorName.setText("By " + stories.get(position).getAuthor());

        holder.ageRange.setText("For kids ages " + stories.get(position).getAge());
        holder.num_likes.setText(String.valueOf(stories.get(position).getLikesCount()));
        holder.num_dislikes.setText(String.valueOf(stories.get(position).getDislikesCount()));

        int storyId = stories.get(position).getId();

        holder.list_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int story_id = stories.get(position).getId();
                Intent intent = new Intent(context, SingleStoryActivity.class);
                intent.putExtra("story_id", story_id);
                context.startActivity(intent);
            }
        });

        holder.like.setTag(R.drawable.ic_thumb_up_black_24dp);    //When you change the drawable
        holder.dislike.setTag(R.drawable.ic_thumb_down_black_24dp);

        Story story = stories.get(position);

        boolean isBookmarked = bookmarked.isAlreadyBookmarked(storyId, position);
        boolean check = Prefs.getBoolean(String.valueOf(storyId),false);
        Log.e("STORYYyyyyyyyyyy", isBookmarked + "");
        holder.bookmark.setActivated(story.isBookmark());
        /*if(check){
            story.setBookmark(true);
            holder.bookmark.setActivated(story.isBookmark());
        }else{
            story.setBookmark(false);
            holder.bookmark.setActivated(story.isBookmark());
        }*/


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
                String title = stories.get(position).getTitle();
                String body = stories.get(position).getBody();
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
                boolean checked = bookmarked.onBookmarkAdded(storyId);
                holder.bookmark.setActivated(!holder.bookmark.isActivated());
                if(checked){
                    Common.updateSharedPref(storyId,true);
                }else{
                    Common.updateSharedPref(storyId,false);
                }
                if(!holder.bookmark.isActivated()){
                    Common.updateSharedPref(storyId,false);
                    deleteStory(context, storyId);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return stories.size();
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

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public Filter getFilter() {
        return storyFilter;
    }

    private Filter storyFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Story> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(stories);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Story item : stories) {
                    if (item.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            stories.clear();
            stories.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

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
        ImageButton bookmark;
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

        boolean isAlreadyBookmarked(int storyId, int pos);
    }

    public interface StorySearch{
        void onStorySearched(String query);
    }

    static void deleteStory(Context context, int storyId){
        Api service;
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
    }
}
