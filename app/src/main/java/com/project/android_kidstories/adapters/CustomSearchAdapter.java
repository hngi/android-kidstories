package com.project.android_kidstories.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CustomSearchAdapter extends ArrayAdapter {

    private List<String> dataList;
    private Context mContext;
    private int searchResultItemLayout;

    public CustomSearchAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        dataList = objects;
        mContext = context;
        searchResultItemLayout = resource;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public String getItem(int position) {
        return dataList.get(position);
    }

    @NotNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(searchResultItemLayout, parent, false);
        }

        TextView resultItem = (TextView) view.findViewById(android.R.id.text1);
        resultItem.setText(getItem(position));
        return view;
    }
}
