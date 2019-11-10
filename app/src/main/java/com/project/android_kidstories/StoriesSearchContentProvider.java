package com.project.android_kidstories;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.project.android_kidstories.data.SearchData;
import com.project.android_kidstories.data.model.Story;

public class StoriesSearchContentProvider extends ContentProvider {

    private static final String STORIES = "stories/"+ SearchManager.SUGGEST_URI_PATH_QUERY+"/*";

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI("com.project.android_kidstories.search", STORIES, 1);
    }

    private static String[] matrixCursorColumns = {"_id",
            SearchManager.SUGGEST_COLUMN_TEXT_1,
            SearchManager.SUGGEST_COLUMN_ICON_1,
            SearchManager.SUGGEST_COLUMN_INTENT_DATA};

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        String queryType = "";
        switch(uriMatcher.match(uri)){
            case 1 :
                String query = uri.getLastPathSegment().toLowerCase();
                return getSearchResultsCursor(query);
            default:
                return null;
        }
    }

    private MatrixCursor getSearchResultsCursor(String searchString){
        MatrixCursor searchResults =  new MatrixCursor(matrixCursorColumns);
        Object[] mRow = new Object[4];
        int counterId = 0;
        if(searchString != null){
            searchString = searchString.toLowerCase();

            for(Story rec : SearchData.getStories()){
                if(rec.getTitle().toLowerCase().contains(searchString)){
                    mRow[0] = ""+counterId++;
                    mRow[1] = rec.getTitle();

                    mRow[2] = rec.getImageUrl(); /*getContext().getResources().getDrawable(R.drawable.account_icon);getContext().getResources().getIdentifier(getStoryTitle(rec.getTitle()),
                            "drawable", getContext().getPackageName());*/
                    mRow[3] = rec.getId()+"&"+rec.getTitle();

                    searchResults.addRow(mRow);
                }
            }
        }
        return searchResults;
    }

    private String getStoryTitle(String suggestion){
        String suggestionWords[] = suggestion.split(" ");
        return suggestionWords[0].toLowerCase();
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
