package com.project.android_kidstories.data.source.local.relational;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.project.android_kidstories.data.model.ReadStory;

@Dao
public interface ReadStoryDao {

    @Insert
    void insertReadStory(ReadStory readStory);

    @Query("SELECT * FROM readstories WHERE storyid=:id")
    LiveData<ReadStory> getStoryForId(String id);
}
