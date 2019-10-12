package com.project.android_kidstories.Api.Responses.story;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.project.android_kidstories.Model.Story;

import java.util.List;

/**
 * @author .: Ehma Ugbogo
 * @email ..: ehmaugbo@gmail.com
 * @created : 11/10/19
 */

public class StoryBaseResponse {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("code")
    @Expose
    private int code;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Story data;
    @SerializedName("reaction")
    @Expose
    private String reaction;
    @SerializedName("bookmark")
    @Expose
    private boolean bookmark;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Story getData() {
        return data;
    }

    public void setData(Story data) {
        this.data = data;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public boolean isBookmark() {
        return bookmark;
    }

    public void setBookmark(boolean bookmark) {
        this.bookmark = bookmark;
    }
}
