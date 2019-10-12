package com.project.android_kidstories.DataStore;

import android.content.Context;

import com.project.android_kidstories.Model.Story;

import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * @author .: Ehma Ugbogo
 * @email ..: ehmaugbogo@gmail.com
 * @created : 10/10/19
 */


@androidx.room.Database(entities = {Story.class},version = 4,exportSchema = false)
public abstract class StoryDatabase extends RoomDatabase {
    private static StoryDatabase INSTANCE;

    public static synchronized StoryDatabase getInstance(Context context){
        if(INSTANCE==null){
            return Room.databaseBuilder(context, StoryDatabase.class,"story_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return INSTANCE;
    }

    public abstract StoryDao storyDao();
}

