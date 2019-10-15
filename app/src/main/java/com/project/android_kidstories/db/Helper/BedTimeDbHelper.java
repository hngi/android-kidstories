package com.project.android_kidstories.db.Helper;

import android.bluetooth.le.AdvertiseData;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.project.android_kidstories.Model.Story;

public class BedTimeDbHelper extends SQLiteOpenHelper {


    public static final int DATABASE_VERSION =2;
    public static final String DATABASE_NAME = "bedtimestories.db";
    /*
    private static final String SQL_CREATE_ADDSTORIES =
            "CREATE TABLE " + AddStories.AddStoriesColumn.TABLE_NAME + "(" +
                    AddStories.AddStoriesColumn._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    AddStories.AddStoriesColumn.TITLE+" TEXT," +
                    AddStories.AddStoriesColumn.AUTHOR +" AUTHOR,"+
                    AddStories.AddStoriesColumn.CATEGORY +" CATEGORY,"+
                    AddStories.AddStoriesColumn.BODY+" BODY,"+
                    AddStories.AddStoriesColumn.IMAGE+" IMAGE)";*/

    private static final String SQL_CREATE_ADD_USERS =
            "CREATE TABLE " + AddUsers.AddUsersColumn.TABLE_NAME + "(" +
                    AddUsers.AddUsersColumn._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    AddUsers.AddUsersColumn.USER_ID+" INTEGER,"+
                    AddUsers.AddUsersColumn.IMAGE+" BLOB," +
                    AddUsers.AddUsersColumn.FIRST_NAME +" TEXT,"+
                    AddUsers.AddUsersColumn.LAST_NAME +" TEXT)";

   /* private static final String SQL_DELETE_USER_ENTRY =
            "DROP TABLE IF EXISTS " + AddStories.AddStoriesColumn.TABLE_NAME;*/

    /*
    public BedTimeDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }*/

    public BedTimeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        // Log.d("table", CREATE_TABLE_STUDENTS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL(SQL_CREATE_ADDSTORIES);
        db.execSQL(SQL_CREATE_ADD_USERS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL(SQL_CREATE_ADDSTORIES);
        db.execSQL(SQL_CREATE_ADD_USERS);
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
    /*
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
*/
    public void storeUserImage(byte[] image, Context context){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("image", image);
        //values.put("first_name", user.getFirstName());
        //values.put("last_name", user.getLastName());

       // database.insertWithOnConflict(AddUsers.AddUsersColumn.TABLE_NAME, null,values,SQLiteDatabase.CONFLICT_IGNORE);
        long rowInserted = database.insert(AddUsers.AddUsersColumn.TABLE_NAME, null, values);
        if(rowInserted != -1)
            Toast.makeText(context, "New row added, row id: " + rowInserted, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(context, "Something wrong", Toast.LENGTH_SHORT).show();

    }

    public byte[] getUserImage(int clientId){
        SQLiteDatabase db = this.getWritableDatabase();

        byte[] image = null;
        String whereclause = " WHERE "+ AddUsers.AddUsersColumn._ID+" = "+clientId;
        Cursor cursor = db.rawQuery("SELECT image FROM "+AddUsers.AddUsersColumn.TABLE_NAME+whereclause, null);
        if (cursor.moveToFirst()){
            do {
                // Passing values
                image = cursor.getBlob(cursor.getColumnIndex(AddUsers.AddUsersColumn.IMAGE));
                //session.setTotalPowerUsage(" ");

            } while(cursor.moveToNext());
        }
        cursor.close();
        return image;
    }

    public int getLastId(String tablename) {
        SQLiteDatabase db = this.getWritableDatabase();
        int id = 0;

            String query = "SELECT id FROM " + tablename + " ORDER BY id DESC LIMIT 1";
            Cursor cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.getCount() != 0) {
                if (cursor.moveToFirst()) {
                    do {
                        id = cursor.getInt(cursor.getColumnIndex("id"));
                    } while (cursor.moveToNext());
                }
            }
            cursor.close();

        return id;
    }


}
