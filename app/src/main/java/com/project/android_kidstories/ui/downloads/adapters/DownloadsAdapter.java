package com.project.android_kidstories.ui.downloads.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.project.android_kidstories.R;
import com.project.android_kidstories.data.model.Story;
import com.project.android_kidstories.data.source.local.relational.database.StoryLab;
import com.project.android_kidstories.ui.story_viewing.SingleStoryActivity;

import java.util.Objects;

import static com.project.android_kidstories.utils.CommonUtils.loadBitmap;

public class DownloadsAdapter extends ListAdapter<Story, DownloadsAdapter.ViewHolder> {

    private Context context;

    public DownloadsAdapter(Context context) {
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

        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_download_stories, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Story currentStory = getCurrentList().get(position);

        Log.d("GLOBAL_SCOPE", "In onBindViewHolder");

        holder.storyTitle.setText(currentStory.getTitle());
        holder.storyAuthor.setText(String.format("by %s", currentStory.getAuthor()));


        Bitmap bitmap = loadBitmap(context, currentStory.getTitle() + ".png");
        holder.storyImage.setImageBitmap(bitmap);

        holder.storyDescription.setText(currentStory.getBody());

        holder.itemView.setOnClickListener(v -> {
            // Navigate to Single Story Activity
            Intent intent = new Intent(context, SingleStoryActivity.class);
            intent.putExtra(SingleStoryActivity.STORY_ID_KEY, currentStory.getId());
            context.startActivity(intent);
        });

        holder.remove.setOnClickListener(v -> {
            new AlertDialog.Builder(context, R.style.AppTheme_Dialog).setTitle("Delete Story")
                    .setMessage("Do you want to delete this story?")
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        StoryLab.get(context).deleteStory(currentStory);
                        context.deleteFile(currentStory.getTitle() + ".png");
                        notifyItemRemoved(position);
                        submitList(StoryLab.get(context).getStories());
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
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
        private ImageView remove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            storyImage = itemView.findViewById(R.id.img_itemimage_downloads);
            storyTitle = itemView.findViewById(R.id.txt_itemtitle_downloads);
            storyAuthor = itemView.findViewById(R.id.txt_itemauthor_downloads);
            storyDescription = itemView.findViewById(R.id.txt_itemdesc_downloads);
            remove = itemView.findViewById(R.id.img_itemremove_downloads);
        }
    }
}
