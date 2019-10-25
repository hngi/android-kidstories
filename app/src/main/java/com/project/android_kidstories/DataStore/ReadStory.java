package com.project.android_kidstories.DataStore;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import org.jetbrains.annotations.NotNull;

@Entity(tableName = "readstories")
public class ReadStory {

    @PrimaryKey
    @NonNull
    String storyid;

    public ReadStory(@NotNull String storyid) {
        this.storyid = storyid;
    }
}
