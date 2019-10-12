package com.project.android_kidstories.db;

import android.provider.BaseColumns;

public class AddStories {
    public AddStories(){

    }
    public static abstract class AddStoriesColumn implements BaseColumns{

        public static final String TABLE_NAME = "Stories";
        public static final String _ID = "Story_id";
        public static final String TITLE = "title";
        public static final String AUTHOR = "author";
        public static final String BODY = "body";
        public static final String CATEGORY = "category";
        public static final String IMAGE = "image";

    }
}
