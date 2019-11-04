package com.project.android_kidstories;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.project.android_kidstories.data.model.Comment;
import de.hdodenhof.circleimageview.CircleImageView;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> comments;
    private Context context;
    public final String TAG = "CommentAdapter";

    public CommentAdapter(List<Comment> comments, Context context) {
        this.context = context;
        this.comments = comments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_story_layout, parent, false);
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

        TextView commentAuthorFirstName, commentAuthorLastName, comment, commentDate;
        CircleImageView commentImage;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            commentImage = itemView.findViewById(R.id.comment_image);
            commentAuthorFirstName = itemView.findViewById(R.id.comment_author_first_name);
            commentAuthorLastName = itemView.findViewById(R.id.comment_author_last_name);
            comment = itemView.findViewById(R.id.comment);
            commentDate = itemView.findViewById(R.id.comment_date);
        }

        void bind(int position) {
            Comment c = comments.get(position);
            Glide.with(context)
                    .load(c.getUser().getImage())
                    .placeholder(R.drawable.ic_android_black_24dp)
                    .into(commentImage);
            commentAuthorFirstName.setText(c.getUser().getFirstName());
            commentAuthorLastName.setText(c.getUser().getLastName());
            String time = comments.get(position).getCreatedAt();
            commentDate.setText(timeFormatter(time));
            comment.setText(c.getBody());

            Log.d(TAG, "bind: Last Name: " + comments.get(position).getUser().getLastName());
            Log.d(TAG, "bind: First Name: " + comments.get(position).getUser().getFirstName());
            Log.d(TAG, "bind: Email Address: " + comments.get(position).getUser().getEmail());
            Log.d(TAG, "bind: Name: " + comments.get(position).getUser().getName());
            Log.d(TAG, "bind: Image URL: " + comments.get(position).getUser().getImage());
        }

        private String timeFormatter(String time) {
            String secondTimeConversion = "";
            String hour = "";
            String minute = "";
            String firstTimeConversion = "";
            String convertedHour = "";
            String convertedTime = "";
            for (int x = 0; x < time.length(); x++) {
                if (Character.isWhitespace(time.charAt(x))) {
                    firstTimeConversion = time.substring(x + 1);
                    break;
                }
            }

            for (int y = 0; y < firstTimeConversion.length(); y++) {
                if (firstTimeConversion.charAt(y) == ':') {
                    secondTimeConversion = firstTimeConversion.substring(0, y);
                }
            }

            for (int z = 0; z < secondTimeConversion.length(); z++) {
                if (secondTimeConversion.charAt(z) == ':') {
                    hour = secondTimeConversion.substring(0, z);
                }
            }

            for (int z = 0; z < secondTimeConversion.length(); z++) {
                if (secondTimeConversion.charAt(z) == ':') {
                    minute = secondTimeConversion.substring(z + 1);
                }
            }

            // Database time is in GMT time, add 1 hour to convert to local time
            String localHour = String.valueOf(Integer.parseInt(hour) + 1);

            // AM/PM Conversion
            if (Integer.parseInt(localHour) > 12) {
                convertedHour = String.valueOf(Integer.parseInt(localHour) - 12);
                convertedTime = convertedHour + ":" + minute + " " + "pm";
                return convertedTime;
            } else {
                convertedTime = localHour + ":" + minute + " " + "am";
                return convertedTime;
            }
        }
    }
}
