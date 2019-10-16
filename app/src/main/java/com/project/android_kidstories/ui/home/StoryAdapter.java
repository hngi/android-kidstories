package com.project.android_kidstories.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.android_kidstories.DataStore.ApiViewmodel;
import com.project.android_kidstories.Model.Story;
import com.project.android_kidstories.R;
import com.project.android_kidstories.Utils.StoryEffect;

import java.util.ArrayList;
import java.util.List;


/**
 * @author .: Ehma Ugbogo
 * @email ..: ehmaugbogo@gmail.com
 * @created : 12/10/19
 */


public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {
    private final ApiViewmodel apiViewmodel;
    private List<Story> stories = new ArrayList<>();

    private OnStoryClickListener listener;

    public interface OnStoryClickListener {
        void onStoryClick(Story story);
    }

    public StoryAdapter(ApiViewmodel apiViewmodel) {
        this.apiViewmodel = apiViewmodel;
    }

    public void setOnStoryClickListener(OnStoryClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Story currentStory = stories.get(position);
        holder.title.setText(currentStory.getTitle());
        holder.author.setText(currentStory.getAuthor());
        holder.ageRange.setText(String.format("For Kids %s years old", currentStory.getAge()));
        holder.likeCount.setText(currentStory.getLikesCount());
        holder.dislikeCount.setText(currentStory.getDislikesCount());

        if (currentStory.isBookmark()) {
            holder.bookmarkIcon.setImageResource(R.drawable.ic_bookmark_click_24dp);
        } else {
            holder.bookmarkIcon.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
        }


    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    public void summitStories(List<Story> stories) {
        this.stories = stories;
        if (stories.size() > 1) {
            savaStoriesForOffline(stories);
        }
    }

    private void savaStoriesForOffline(List<Story> stories) {
        apiViewmodel.getRepository().deleteAllOfflineStories();
        for (Story story : stories) {
            apiViewmodel.getRepository().insertOfflineStory(story);
        }
    }

    public Story getStoryAtPosition(int position) {
        return stories.get(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView image;
        private TextView title;
        private TextView author;
        private TextView ageRange;
        private ImageView likeIcon;
        private TextView likeCount;
        private ImageView disikeIcon;
        private TextView dislikeCount;
        private ImageView bookmarkIcon;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.story_item_image);
            title = itemView.findViewById(R.id.story_item_title);
            author = itemView.findViewById(R.id.story_item_authur);
            ageRange = itemView.findViewById(R.id.story_item_age_range);
            likeIcon = itemView.findViewById(R.id.story_item_like_icon);
            likeCount = itemView.findViewById(R.id.story_item_like_count);
            disikeIcon = itemView.findViewById(R.id.story_item_dislike_icon);
            dislikeCount = itemView.findViewById(R.id.story_item_dislike_count);
            bookmarkIcon = itemView.findViewById(R.id.story_item_bookmark_icon);

            itemView.setOnClickListener(this);

        }

        //TODO: make api call to effect online change
        //TODO Ehma Implement WorkManager
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.story_item_like_icon:
                    effectChangeOnStory(StoryEffect.LIKED);
                    break;
                case R.id.story_item_dislike_icon:
                    effectChangeOnStory(StoryEffect.DISLIKED);
                    break;
                case R.id.story_item_bookmark_icon:
                    effectChangeOnStory(StoryEffect.BOOKMARK);
                    break;
            }
        }

        private void effectChangeOnStory(StoryEffect effect) {
            Story story = stories.get(getAdapterPosition());
            switch (effect){
                case LIKED:
                    if(story.isLiked()){
                        story.setLiked(false);
                        story.setLikesCount(story.getLikesCount()+1);
                    } else {story.setLiked(true);story.setLikesCount(story.getLikesCount()-1);}
                    break;
                case DISLIKED:
                    if(story.isDisliked()){
                        story.setDisliked(false);
                        story.setDislikesCount(story.getDislikesCount()+1);
                    } else {story.setDisliked(true);story.setDislikesCount(story.getDislikesCount()-1);}
                    break;
                case BOOKMARK:
                    if(story.isBookmark()){
                        story.setBookmark(false);
                    } else {story.setBookmark(true);}
                    break;
                    default:
                        stories.set(getAdapterPosition(),story);
                        notifyDataSetChanged();

            }
        }


    }
}
