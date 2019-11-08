package com.project.android_kidstories.data.source.remote.response_models.story.reaction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.android_kidstories.data.model.Story;

import java.util.List;

public class StoryPageResponse {

    @SerializedName("data")
    @Expose
    List<Story> stories;

    @SerializedName("count")
    @Expose
    int count;

    @SerializedName("total")
    @Expose
    int total;

    @SerializedName("prev")
    @Expose
    String prev;

    @SerializedName("next")
    @Expose
    String next;

    public List<Story> getStories() {
        return stories;
    }

    public void setStories(List<Story> stories) {
        this.stories = stories;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getPrev() {
        return prev;
    }

    public void setPrev(String prev) {
        this.prev = prev;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }
}
