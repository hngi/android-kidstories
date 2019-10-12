package com.project.android_kidstories.adapters;


import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.project.android_kidstories.R;
import com.project.android_kidstories.fragments.NewStoriesFragment;


import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.StoryViewHolder>{


    public Context mContext;
    public List<String> storyTitle;
    public List<Integer> storyPic;
    private StoryListener storyListener;


    public RecyclerAdapter(Context mContext, List<String> storyTitle, List<Integer> storyPic) {
        this.mContext = mContext;
        this.storyTitle = storyTitle;
        this.storyPic = storyPic;
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item, viewGroup, false);
        return new StoryViewHolder(mView);

    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder storyViewHolder, int i) {
storyViewHolder.mImage.setImageResource(storyPic.get(i));
storyViewHolder.mTitle.setText(storyTitle.get(i));

    }

    @Override
    public int getItemCount() {
        return storyTitle.size() ;
    }

    public void setStoryListener(StoryListener listener){
        storyListener = listener;
    }


    class StoryViewHolder extends RecyclerView.ViewHolder {

        ImageView mImage;
        TextView mTitle;

        StoryViewHolder(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.recyclerImage);
            mTitle = itemView.findViewById(R.id.recyclerName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    storyListener.onStoryClick();
                }
            });
        }
    }


    public interface StoryListener{

        void onStoryClick();

    }

}
