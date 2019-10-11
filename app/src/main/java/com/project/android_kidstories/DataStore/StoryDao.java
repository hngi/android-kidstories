
package com.project.android_kidstories.DataStore;

import com.project.android_kidstories.Model.Story;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

/**
 * @author .: Ehma Ugbogo
 * @email ..: ehmaugbogo@gmail.com
 * @created : 10/10/19
 */


@Dao
public interface StoryDao {

    @Insert
    Long insertStory(Story story);

    @Update
    void updateStory(Story story);

    @Delete
    void deleteStory(Story story);

    @Query("DELETE FROM story")
    void deleteAllStories();

    @Query("SELECT * FROM story")
    LiveData<List<Story>> getAllStories();
}
