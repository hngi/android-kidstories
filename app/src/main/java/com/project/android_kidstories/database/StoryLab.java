package com.project.android_kidstories.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.project.android_kidstories.Model.Story;
import com.project.android_kidstories.database.StoryDbSchema.StoryTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StoryLab { // A singleton class
    private static StoryLab sStoryLab;

    //i made the story list and getStory() STATIC

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static StoryLab get(Context context){
        if(sStoryLab==null){
            sStoryLab = new StoryLab(context);
        }

        return sStoryLab;
    }

    private StoryLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new StoryBaseHelper(mContext)
                .getWritableDatabase();


    }

    public void addStory(Story story){
        ContentValues values = getContentValues(story);
        mDatabase.insert(StoryTable.NAME,null,values);
    }

    public void deleteStory(Story story){
        mDatabase.delete(
                StoryTable.NAME,
                StoryTable.Cols.ID + " = ?",
                new String[]{String.valueOf(story.getId())}
        );
    }

    public List<Story> getStories(){
        List<Story> storyList = new ArrayList<>();
        StoryCursorWrapper cursor = queryStories(null,null);
        // mCOUNTForCURSOR=cursor.getCount();
        try{
            cursor.moveToFirst();
            //mCOUNTForCURSOR=cursor.getCount();
            while(!cursor.isAfterLast()){
                storyList.add(cursor.getStory());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return storyList;
    }

    //    public  Story getStory(int id){
//        //TODO : NOT WORKING
//        StoryCursorWrapper cursor = queryStories(
//                StoryTable.Cols.ID + " = ?",
//                new String[] { Integer.toString(id) }
//        );
//
//        try{
//            if(cursor.getCount()==0){
//                return null;
//            }
//            cursor.moveToFirst();
//            return cursor.getStory();
//        }finally{
//            cursor.close();
//        }
//    }
    public  Story getStory(String title){
        StoryCursorWrapper cursor = queryStories(
                StoryTable.Cols.TITLE + " = ?",
                new String[] { title }
        );

        try{
            if(cursor.getCount()==0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getStory();
        }finally{
            cursor.close();
        }
    }

//    public File getPhotoFile(Story story){
//        File filesDir =mContext.getFilesDir();
//        return new File (filesDir,story.getPhotoFileName());
//    }




    //a method that shuttle stor into ContentValues
    private static ContentValues getContentValues(Story story){
        ContentValues values = new ContentValues();
        values.put(StoryTable.Cols.ID, story.getId().intValue());
        values.put(StoryTable.Cols.TITLE,story.getTitle());
        values.put(StoryTable.Cols.AUTHOR, story.getAuthor());
        values.put(StoryTable.Cols.BODY, story.getBody());
        values.put(StoryTable.Cols.AGE,story.getAge());

        return values;
    }

    private  StoryCursorWrapper queryStories(String where, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                StoryTable.NAME,
                null,
                where,
                whereArgs,
                null,
                null,
                null,null

        );

        return new StoryCursorWrapper(cursor);
    }
}