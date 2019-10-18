package com.project.android_kidstories.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.project.android_kidstories.Api.Responses.Category.CategoriesAllResponse;
import com.project.android_kidstories.R;
import com.project.android_kidstories.StoryListingActivity;

/**
 * @author .: Oluwajuwon Fawole
 * @created : 16/10/19
 */
public class RecyclerCategoryAdapter extends RecyclerView.Adapter<RecyclerCategoryAdapter.CustomViewHolder>{

        private CategoriesAllResponse categoryList;
        private Context context;


        public RecyclerCategoryAdapter(Context context, CategoriesAllResponse categoryList){
            this.context = context;
            this.categoryList = categoryList;
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

        @Override
        public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.category_list, parent, false);
            return new CustomViewHolder(view);
        }

        @Override
        public void onBindViewHolder(CustomViewHolder holder, int position) {
            holder.categoryName.setText(categoryList.getData().get(position).getName());

            Glide.with(context).load(categoryList.getData().get(position).getImageUrl()).into(holder.categoryImage);

            holder.categoryImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int categoryId = categoryList.getData().get(position).getId();
                    String categoryName = categoryList.getData().get(position).getName();
                    Intent intent = new Intent(context, StoryListingActivity.class);
                    intent.putExtra("categoryId", categoryId );
                    intent.putExtra("categoryName",categoryName);
                    context.startActivity(intent);
                }
            });


        }

        @Override
        public int getItemCount() {
            return categoryList.getData().size();
        }
}
