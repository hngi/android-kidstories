package com.project.android_kidstories.ui.home.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.project.android_kidstories.R;
import com.project.android_kidstories.data.model.Story;
import com.project.android_kidstories.data.source.local.preferences.SharePref;
import com.project.android_kidstories.data.source.remote.api.Api;
import com.project.android_kidstories.data.source.remote.api.RetrofitClient;
import com.project.android_kidstories.data.source.remote.response_models.story.reaction.ReactionResponse;
import com.project.android_kidstories.ui.story_viewing.SingleStoryActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Objects;


public class ExploreAdapter extends ListAdapter<Story, ExploreAdapter.ViewHolder> {

    private Context context;
    private OnBookmark onBookmarkListener;
    private FragmentActivity a;

    public ExploreAdapter(Fragment fragment) {
        super(new DiffUtil.ItemCallback<Story>() {
            @Override
            public boolean areItemsTheSame(@NonNull Story oldItem, @NonNull Story newItem) {
                return oldItem == newItem;
            }

            @Override
            public boolean areContentsTheSame(@NonNull Story oldItem, @NonNull Story newItem) {
                return Objects.equals(oldItem.getId(), newItem.getId());
            }
        });

        try {
            onBookmarkListener = (OnBookmark) fragment;
            this.context = fragment.getContext();
            this.a = fragment.getActivity();

        } catch (IllegalStateException ise) {
            Toast.makeText(context, "Context must implement OnBookmark", Toast.LENGTH_SHORT).show();
            throw ise;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Story currentStory = getCurrentList().get(position);

        Log.d("GLOBAL_SCOPE", "In onBindViewHolder");

        holder.storyTitle.setText(currentStory.getTitle());
        holder.storyAuthor.setText(String.format("by %s", currentStory.getAuthor()));

        if (currentStory.isBookmark()) {
            holder.bookmark.setSelected(true);
        } else {
            holder.bookmark.setSelected(false);
        }

        holder.likeCount.setText(String.valueOf(currentStory.getLikesCount()));
        holder.dislikeCount.setText(String.valueOf(currentStory.getDislikesCount()));

        String reaction = currentStory.getReaction();
        holder.thumbsup.setSelected(reaction.equals("1"));
        holder.thumbsdown.setSelected(reaction.equals("0"));

        holder.ageRange.setText(String.format("ages %s", currentStory.getAge()));

        Glide.with(holder.itemView)
                .load(currentStory.getImageUrl())
                .into(holder.storyImage);

        holder.itemView.setOnClickListener(v -> {
            // Navigate to Single Story Activity
            Intent intent = new Intent(context, SingleStoryActivity.class);
            intent.putExtra(SingleStoryActivity.STORY_ID_KEY, currentStory.getId());
            intent.putExtra(SingleStoryActivity.STORY_NAME_KEY, currentStory.getTitle());
            context.startActivity(intent);
        });

        //holder.storyDescription.setText(currentStory.getBody());

        holder.bookmark.setOnClickListener(view -> {
            currentStory.setBookmark(!currentStory.isBookmark());
            // Save
            setBookmark(holder, currentStory);
        });

        setReactions(holder, currentStory);
    }

    private void setReactions(ViewHolder holder, Story currentStory) {
        SharePref sharePref = SharePref.getInstance(a.getApplication());
        final String token = "Bearer " + sharePref.getUserToken();
        Api service = RetrofitClient.getInstance().create(Api.class);

        holder.thumbsup.setOnClickListener(v -> {

            holder.thumbsup.setSelected(!holder.thumbsup.isSelected());
            holder.thumbsdown.setSelected(false);

            // Try to effect the change online
            service.likeStory(token, currentStory.getId())
                    .enqueue(new Callback<ReactionResponse>() {
                        @Override
                        public void onResponse(Call<ReactionResponse> call, Response<ReactionResponse> response) {
                            if (response.isSuccessful()) {
                                // Update like count
                                ReactionResponse rr = response.body();
                                if (rr == null) {
                                    holder.thumbsup.setSelected(!holder.thumbsup.isSelected());
                                    Toast.makeText(context, "Could not like story", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                holder.likeCount.setText(String.valueOf(rr.getLikesCount()));
                                holder.dislikeCount.setText(String.valueOf(rr.getDislikesCount()));
                                Toast.makeText(context, "Story liked", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ReactionResponse> call, Throwable t) {
                            holder.thumbsup.setSelected(!holder.thumbsup.isSelected());
                            Toast.makeText(context, "Could not like story, check internet connection", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        holder.thumbsdown.setOnClickListener(v -> {

            holder.thumbsdown.setSelected(!holder.thumbsdown.isSelected());
            holder.thumbsup.setSelected(false);

            // Try to effect the change online
            service.dislikeStory(token, currentStory.getId())
                    .enqueue(new Callback<ReactionResponse>() {
                        @Override
                        public void onResponse(Call<ReactionResponse> call, Response<ReactionResponse> response) {
                            if (response.isSuccessful()) {
                                // Update like count
                                ReactionResponse rr = response.body();
                                if (rr == null) {
                                    holder.thumbsdown.setSelected(!holder.thumbsdown.isSelected());
                                    Toast.makeText(context, "Could not dislike story", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                holder.likeCount.setText(String.valueOf(rr.getLikesCount()));
                                holder.dislikeCount.setText(String.valueOf(rr.getDislikesCount()));
                                Toast.makeText(context, "Story disliked", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ReactionResponse> call, Throwable t) {
                            holder.thumbsdown.setSelected(!holder.thumbsdown.isSelected());
                            Toast.makeText(context, "Could not dislike story, check internet connection", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alt_item_explore_stories, parent, false);
        return new ViewHolder(itemView);
    }

    private void setBookmark(ViewHolder holder, Story story) {
        if (story.isBookmark()) {
            holder.bookmark.setSelected(true);
        } else {
            holder.bookmark.setSelected(false);
        }

        onBookmarkListener.onBookmark(story);
    }

    public interface OnBookmark {
        void onBookmark(Story story);
    }

    @Override
    public int getItemCount() {
        return getCurrentList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView storyImage;
        private TextView storyTitle;
        private TextView storyAuthor;
        private TextView storyDescription;
        private ImageView bookmark;
        private TextView likeCount;
        private TextView dislikeCount;
        private TextView ageRange;
        private ImageView thumbsup;
        private ImageView thumbsdown;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            storyImage = itemView.findViewById(R.id.img_itemimage_explore);
            storyTitle = itemView.findViewById(R.id.txt_itemtitle_explore);
            storyAuthor = itemView.findViewById(R.id.txt_itemauthor_explore);
            storyDescription = itemView.findViewById(R.id.txt_itemdesc_explore);
            bookmark = itemView.findViewById(R.id.img_itembookmarked_explore);
            likeCount = itemView.findViewById(R.id.txt_itemlikecount_explore);
            dislikeCount = itemView.findViewById(R.id.txt_itemdislikecount_explore);
            ageRange = itemView.findViewById(R.id.txt_itemage_explore);
            thumbsup = itemView.findViewById(R.id.img_itemlikecount_explore);
            thumbsdown = itemView.findViewById(R.id.img_itemdislikecount_explore);
        }
    }
}
