
package com.project.android_kidstories.data.source.local.relational;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.project.android_kidstories.data.model.Story;

import java.util.List;

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
