package com.project.android_kidstories.data.source.local.relational.database;

public class StoryDbSchema {
    public static final class StoryTable{
        //there could be another table in this schema class
        //hence the need for the NAME variable;
        public static final String NAME = "stories";
//          lesson : A story table will have columns which you can
//              distinguish by Cols.TITLE, or using any other property

        //   lesson.. the schema carries a table ..
        //the table can have as many columns as possible
        //but each must have 7 properties;
        public static final class Cols{
            public static final String ID ="id";
            public static final String TITLE ="title";
            public static final String AUTHOR ="author";
            public static final String BODY ="body"  ;
            public static final String AGE= "age";
            public static final String IMAGE= "image";
            // public static final String CATEGORY= "category";

        }
    }
}
