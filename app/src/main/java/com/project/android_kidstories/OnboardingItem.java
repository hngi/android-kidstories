package com.project.android_kidstories;

public class OnboardingItem {
    private String title, Description;
    private   int imageResourceId;

    public OnboardingItem(String title, String description, int imageResourceId) {
        this.title = title;
        Description = description;
        this.imageResourceId = imageResourceId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return Description;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}
