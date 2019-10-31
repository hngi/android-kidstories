package com.project.android_kidstories.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.project.android_kidstories.database.StoryDbSchema.StoryTable;

import androidx.annotation.Nullable;

public class StoryBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION= 1;
    private static final String DATABASE_NAME = "storyBase.Db";


    public StoryBaseHelper(Context context){
        super(context,DATABASE_NAME,null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + StoryDbSchema.StoryTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                StoryTable.Cols.ID + ", " +
                StoryTable.Cols.TITLE + ", " +
                StoryTable.Cols.AUTHOR + ", " +
                /* StoryTable.Cols.CATEGORY + ", " +*/
                StoryTable.Cols.BODY + ", " +
                StoryTable.Cols.AGE + ", " +
                StoryTable.Cols.IMAGE +
                ")"
        );


    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}


