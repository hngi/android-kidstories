package com.project.android_kidstories.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("data")
    @Expose
    private List<Story> dataList = null;

    public List<Story> getDataList() {
        return dataList;
    }

    public void setDataList(List<Story> dataList) {
        this.dataList = dataList;
    }
}
