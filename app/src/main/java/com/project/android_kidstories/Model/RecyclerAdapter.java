package com.project.android_kidstories.Model;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.android_kidstories.R;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemHolder> {
    ArrayList<Integer> images;
    ArrayList<String> author;
    Context context;

    public RecyclerAdapter(Context context, ArrayList<Integer> images, ArrayList<String> author) {
        this.images = images;
        this.author = author;
        this.context = context;

    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list, parent, false);
        ItemHolder itemHolder = new ItemHolder(v);
        return itemHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {

        holder.text1.setText(author.get(position));
        holder.image.setImageResource(images.get(position));

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView text1;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.category_image);
            text1 = itemView.findViewById(R.id.textView8);
        }
    }
}
