
package com.project.android_kidstories.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity
public class Story {

    @PrimaryKey
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("category_id")
    @Expose
    private int categoryId;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("image_name")
    @Expose
    private String imageName;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("story_duration")
    @Expose
    private String storyDuration;
    @SerializedName("is_premium")
    @Expose
    private int isPremium;
    @SerializedName("likes_count")
    @Expose
    private int likesCount;
    @SerializedName("dislikes_count")
    @Expose
    private int dislikesCount;
    @SerializedName("reaction")
    @Expose
    private String reaction;
    @SerializedName("bookmark")
    @Expose
    private boolean bookmark;
    @SerializedName("comments")
    @Expose
    @Ignore
    private Comments comments;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getStoryDuration() {
        return storyDuration;
    }

    public void setStoryDuration(String storyDuration) {
        this.storyDuration = storyDuration;
    }

    public int getIsPremium() {
        return isPremium;
    }

    public void setIsPremium(int isPremium) {
        this.isPremium = isPremium;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getDislikesCount() {
        return dislikesCount;
    }

    public void setDislikesCount(int dislikesCount) {
        this.dislikesCount = dislikesCount;
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

    @Ignore
    public Comments getComments() {
        return comments;
    }

    @Ignore
    public void setComments(Comments comments) {
        this.comments = comments;
    }

}
