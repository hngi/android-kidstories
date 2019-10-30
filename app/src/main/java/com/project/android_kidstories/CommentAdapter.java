package com.project.android_kidstories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.android_kidstories.Model.Comment;
import com.project.android_kidstories.Model.Comments;
import com.project.android_kidstories.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> comments;
    private Context context;


    public CommentAdapter(List<Comment> comments, Context context){
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_story_layout,parent,false);
        return new CommentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }


    public class CommentViewHolder extends RecyclerView.ViewHolder {

        protected TextView commentAuthor, comment, commentDate;
        protected CircleImageView commentImage;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentImage = (CircleImageView) itemView.findViewById(R.id.comment_image);
            commentAuthor = (TextView) itemView.findViewById(R.id.comment_author);
            comment = (TextView) itemView.findViewById(R.id.comment);
            commentDate = (TextView) itemView.findViewById(R.id.comment_date);
        }

        void bind(int position){
            Comment c = comments.get(position);
            Glide.with(context).load(c.getUser().getImage()).into(commentImage);
            commentAuthor.setText(c.getUser().getName());
            comment.setText(c.getBody());
        }
    }

}
