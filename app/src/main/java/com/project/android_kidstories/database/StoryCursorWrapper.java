package com.project.android_kidstories.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import com.project.android_kidstories.data.model.Story;
import com.project.android_kidstories.database.StoryDbSchema.StoryTable;


public class StoryCursorWrapper extends CursorWrapper {

    public StoryCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    //the Story is in this cursorWrapper
    //get it out

    public Story getStory() {
        int id = getInt(getColumnIndex(StoryTable.Cols.ID));
        String title = getString(getColumnIndex(StoryTable.Cols.TITLE));
        String author = getString(getColumnIndex(StoryTable.Cols.AUTHOR));
        String body  = getString(getColumnIndex(StoryTable.Cols.BODY));
        String age = getString(getColumnIndex(StoryTable.Cols.AGE));
        // String category = getString(getColumnIndex(StoryTable.Cols.CATEGORY));

        Story story = new Story();

        story.setId(id);
        story.setTitle(title);
        story.setAuthor(author);
        story.setBody(body);
        story.setAge(age);

        return story ;

    }
}
