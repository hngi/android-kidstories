package com.project.android_kidstories.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import org.jetbrains.annotations.NotNull;

@Entity(tableName = "readstories")
public class ReadStory {

    @PrimaryKey
    @NonNull
    public String storyid;

    public ReadStory(@NotNull String storyid) {
        this.storyid = storyid;
    }
}
