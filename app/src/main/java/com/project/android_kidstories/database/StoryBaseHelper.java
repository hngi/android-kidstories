package com.project.android_kidstories.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class StoryBaseHelper extends SQLiteOpenHelper {

    public StoryBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}


//{
//}
//    spackage com.bignerdranch.android.criminalintent.database;
//
//        import android.content.Context;
//        import android.database.sqlite.SQLiteDatabase;
//        import android.database.sqlite.SQLiteOpenHelper;
//
//        import com.bignerdranch.android.criminalintent.database.CrimeDbSchema.CrimeTable;
//
//public class CrimeBaseHelper extends