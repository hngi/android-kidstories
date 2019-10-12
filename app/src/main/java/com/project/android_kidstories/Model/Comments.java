package com.project.android_kidstories.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author .: Ukeje Emeka
 * @email ..: ukejee3@gmail.com
 * @created : 10/10/19
 */
public class Comments {

    @SerializedName("comments")
    @Expose
    private List<Comment> comments = null;

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
