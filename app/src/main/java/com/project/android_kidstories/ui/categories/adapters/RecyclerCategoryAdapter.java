package com.project.android_kidstories.ui.categories.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.project.android_kidstories.Model.Category;
import com.project.android_kidstories.R;
import com.project.android_kidstories.StoryListingActivity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author .: Oluwajuwon Fawole
 * @created : 16/10/19
 */
public class RecyclerCategoryAdapter extends RecyclerView.Adapter<RecyclerCategoryAdapter.CustomViewHolder> {

    private List<Category> categoryList;
    private Context context;


    public RecyclerCategoryAdapter(Context context, List<Category> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NotNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.category_list, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull CustomViewHolder holder, int position) {
        Category currentCategory = categoryList.get(position);

        String categoryName = currentCategory.getName();
        String categoryImageUrl = currentCategory.getImageUrl();
        int categoryId = categoryList.get(position).getId();

        holder.categoryName.setText(categoryName);
        Glide.with(context).load(categoryImageUrl).into(holder.categoryImage);

        holder.categoryImage.setOnClickListener(view -> {
            Intent intent = new Intent(context, StoryListingActivity.class);
            intent.putExtra("categoryName", categoryName);
            intent.putExtra("categoryId", categoryId);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        TextView categoryName;
        ImageView categoryImage;


        CustomViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            categoryName = mView.findViewById(R.id.textView1_cat);
            categoryImage = mView.findViewById(R.id.category_image);
        }
    }
}
