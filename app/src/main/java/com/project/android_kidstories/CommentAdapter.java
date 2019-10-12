package com.project.android_kidstories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends BaseAdapter {

    private Context context;
//    private ArrayList<ModelListView> dataModelArrayList;

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.comment_story_layout, null, true);

            holder.commentImage = (CircleImageView) convertView.findViewById(R.id.comment_image);
            holder.commentAuthor = (TextView) convertView.findViewById(R.id.comment_author);
            holder.comment = (TextView) convertView.findViewById(R.id.comment);
            holder.commentDate = (TextView) convertView.findViewById(R.id.comment_date);

            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }

//        Picasso.get().load(dataModelArrayList.get(position).getImgURL()).into(holder.commentImage);
//        holder.commentAuthor.setText(dataModelArrayList.get(position).getName());
//        holder.comment.setText(dataModelArrayList.get(position).getCountry());
//        holder.commentDate.setText(dataModelArrayList.get(position).getCity());

        return convertView;
    }

    private class ViewHolder {

        protected TextView commentAuthor, comment, commentDate;
        protected CircleImageView commentImage;
    }
}
