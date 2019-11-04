package com.project.android_kidstories.adapters;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.project.android_kidstories.R;
import com.project.android_kidstories.SingleSavedStoryActivity;
import com.project.android_kidstories.data.model.Story;
import com.project.android_kidstories.database.StoryLab;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

        Bitmap bitmap =loadBitmap(context,storiesList.get(position).getTitle()+".png");
        holder.storyImage.setImageBitmap(bitmap);
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

                new AlertDialog.Builder(context).setTitle("Delete Story")
                        .setMessage("Do you want to delete this story ?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                StoryLab.get(context).deleteStory(storiesList.get(position));
                                deleteFile(storiesList.get(position).getTitle()+".png");
                                reloadStories();
                            }
                        })
                        .setNegativeButton("no" , null)
                        .show();

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

            storyImage = view.findViewById(R.id.saved_story_img);
            storyTitle = view.findViewById(R.id.saved_story_title);
            authorName = view.findViewById(R.id.saved_story_author);


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
    public static Bitmap loadBitmap(Context context, String picName){
        Bitmap b = null;
        FileInputStream fis;
        try {
            fis = context.openFileInput(picName);
            b = BitmapFactory.decodeStream(fis);
        }
        catch (FileNotFoundException e) {
            Log.d("tag", "file not found");
            e.printStackTrace();
        }
        catch (IOException e) {
            Log.d("tag", "io exception");
            e.printStackTrace();
        } finally {

        }
        return b;
    }

    public void deleteFile(String fileName){
        File directory = context.getFilesDir();
        File file = new File(directory, fileName);
        file.delete();

    }

}
