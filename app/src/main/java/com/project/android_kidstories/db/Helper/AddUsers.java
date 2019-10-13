package com.project.android_kidstories.db.Helper;

import android.provider.BaseColumns;

public class AddUsers {
    public AddUsers(){

    }
    public static abstract class AddUsersColumn implements BaseColumns {

        public static final String TABLE_NAME = "Users";
        public static final String _ID = "id";
        public static final String USER_ID = "user_id";
        public static final String IMAGE = "image";
        public static final String FIRST_NAME = "first_name";
        public static final String LAST_NAME = "last_name";
    }
}
