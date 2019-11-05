package com.project.android_kidstories.Api.Responses.story;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.android_kidstories.Model.Story;

import java.util.List;

public class Data {
    public List<Story> getDataList() {
        return dataList;
    }

    public void setDataList(List<Story> dataList) {
        this.dataList = dataList;
    }
    @SerializedName("data")
    @Expose
    private
    List<Story> dataList = null;
}
