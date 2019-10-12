package com.project.android_kidstories.db.Helper;

import android.bluetooth.le.AdvertiseData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import com.project.android_kidstories.Model.Story;
import com.project.android_kidstories.db.AddStories;

public class BedTimeDbHelper extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION =1;
    public static final String DATABASE_NAME = "bedtimestories.db";
    private static final String SQL_CREATE_ADDSTORIES =
            "CREATE TABLE " + AddStories.AddStoriesColumn.TABLE_NAME + "(" +
                    AddStories.AddStoriesColumn._ID+"INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    AddStories.AddStoriesColumn.TITLE+"TEXT," +
                    AddStories.AddStoriesColumn.AUTHOR +"AUTHOR,"+
                    AddStories.AddStoriesColumn.CATEGORY +"CATEGORY,"+
                    AddStories.AddStoriesColumn.BODY+"BODY,"+
                    AddStories.AddStoriesColumn.IMAGE+"IMAGE)";

    private static final String SQL_DELETE_USER_ENTRY =
            "DROP TABLE IF EXISTS" + AddStories.AddStoriesColumn.TABLE_NAME;

    public BedTimeDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ADDSTORIES);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_CREATE_ADDSTORIES);
        onCreate(db);

    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

     // add story
    public void addStory(Story story){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = getContentValues(story);


        //insect story
        long cat_id = db.insertWithOnConflict(AddStories.AddStoriesColumn.TABLE_NAME,
                null,values,SQLiteDatabase.CONFLICT_IGNORE);
    }
    public void updateStory(Story story){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = getContentValues(story);

        db.update(AddStories.AddStoriesColumn.TABLE_NAME,values,
                AddStories.AddStoriesColumn.TITLE +" = ?",new String[] {String.valueOf(story.getTitle())});
    }
    public Story getStoryById(String id){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " +AddStories.AddStoriesColumn.TABLE_NAME + " WHERE "+AddStories.AddStoriesColumn._ID
                + " ='"+id+"'";
        Cursor c = db.rawQuery(query, null);
        Story story = new Story();
        if (c.moveToFirst()){
            story.setId(c.getInt(c.getColumnIndex(AddStories.AddStoriesColumn._ID)));
            story.setTitle(c.getString(c.getColumnIndex(AddStories.AddStoriesColumn.TITLE)));
            story.setAuthor(c.getString(c.getColumnIndex(AddStories.AddStoriesColumn.AUTHOR)));
            story.setBody(c.getString(c.getColumnIndex(AddStories.AddStoriesColumn.BODY)));
            story.setCategoryId(c.getInt(c.getColumnIndex(AddStories.AddStoriesColumn.CATEGORY)));
            story.setImageName(c.getString(c.getColumnIndex(AddStories.AddStoriesColumn.IMAGE)));

        }
        c.close();
        return story;
    }

    public void deleteStory (Story story ){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(AddStories.AddStoriesColumn.TABLE_NAME,
                AddStories.AddStoriesColumn.TITLE +" = ?",
                new String[]{String.valueOf(story.getTitle())});}
    @NonNull
    private ContentValues getContentValues(Story story) {
        ContentValues values = new ContentValues();
        values.put(AddStories.AddStoriesColumn.TITLE,story.getTitle());
        values.put(AddStories.AddStoriesColumn._ID,story.getId());
        values.put(AddStories.AddStoriesColumn.AUTHOR,story.getAuthor());
        values.put(AddStories.AddStoriesColumn.BODY,story.getBody());
        values.put(AddStories.AddStoriesColumn.IMAGE,story.getImageName());
        values.put(AddStories.AddStoriesColumn.CATEGORY,story.getCategoryId());
        return values;
    }


}
