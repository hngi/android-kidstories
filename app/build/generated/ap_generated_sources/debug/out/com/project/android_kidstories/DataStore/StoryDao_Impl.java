package com.project.android_kidstories.DataStore;

import android.database.Cursor;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.project.android_kidstories.Model.Story;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

@SuppressWarnings({"unchecked", "deprecation"})
public final class StoryDao_Impl implements StoryDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfStory;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfStory;

  private final EntityDeletionOrUpdateAdapter __updateAdapterOfStory;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllStories;

  public StoryDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfStory = new EntityInsertionAdapter<Story>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `Story`(`id`,`title`,`body`,`categoryId`,`userId`,`imageUrl`,`imageName`,`age`,`author`,`storyDuration`,`isPremium`,`likesCount`,`dislikesCount`,`reaction`,`bookmark`,`liked`,`disliked`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Story value) {
        stmt.bindLong(1, value.getId());
        if (value.getTitle() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getTitle());
        }
        if (value.getBody() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getBody());
        }
        stmt.bindLong(4, value.getCategoryId());
        stmt.bindLong(5, value.getUserId());
        if (value.getImageUrl() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getImageUrl());
        }
        if (value.getImageName() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getImageName());
        }
        if (value.getAge() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getAge());
        }
        if (value.getAuthor() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getAuthor());
        }
        if (value.getStoryDuration() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getStoryDuration());
        }
        stmt.bindLong(11, value.getIsPremium());
        stmt.bindLong(12, value.getLikesCount());
        stmt.bindLong(13, value.getDislikesCount());
        if (value.getReaction() == null) {
          stmt.bindNull(14);
        } else {
          stmt.bindString(14, value.getReaction());
        }
        final int _tmp;
        _tmp = value.isBookmark() ? 1 : 0;
        stmt.bindLong(15, _tmp);
        final int _tmp_1;
        _tmp_1 = value.isLiked() ? 1 : 0;
        stmt.bindLong(16, _tmp_1);
        final int _tmp_2;
        _tmp_2 = value.isDisliked() ? 1 : 0;
        stmt.bindLong(17, _tmp_2);
      }
    };
    this.__deletionAdapterOfStory = new EntityDeletionOrUpdateAdapter<Story>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `Story` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Story value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfStory = new EntityDeletionOrUpdateAdapter<Story>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `Story` SET `id` = ?,`title` = ?,`body` = ?,`categoryId` = ?,`userId` = ?,`imageUrl` = ?,`imageName` = ?,`age` = ?,`author` = ?,`storyDuration` = ?,`isPremium` = ?,`likesCount` = ?,`dislikesCount` = ?,`reaction` = ?,`bookmark` = ?,`liked` = ?,`disliked` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Story value) {
        stmt.bindLong(1, value.getId());
        if (value.getTitle() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getTitle());
        }
        if (value.getBody() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getBody());
        }
        stmt.bindLong(4, value.getCategoryId());
        stmt.bindLong(5, value.getUserId());
        if (value.getImageUrl() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getImageUrl());
        }
        if (value.getImageName() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getImageName());
        }
        if (value.getAge() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getAge());
        }
        if (value.getAuthor() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getAuthor());
        }
        if (value.getStoryDuration() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getStoryDuration());
        }
        stmt.bindLong(11, value.getIsPremium());
        stmt.bindLong(12, value.getLikesCount());
        stmt.bindLong(13, value.getDislikesCount());
        if (value.getReaction() == null) {
          stmt.bindNull(14);
        } else {
          stmt.bindString(14, value.getReaction());
        }
        final int _tmp;
        _tmp = value.isBookmark() ? 1 : 0;
        stmt.bindLong(15, _tmp);
        final int _tmp_1;
        _tmp_1 = value.isLiked() ? 1 : 0;
        stmt.bindLong(16, _tmp_1);
        final int _tmp_2;
        _tmp_2 = value.isDisliked() ? 1 : 0;
        stmt.bindLong(17, _tmp_2);
        stmt.bindLong(18, value.getId());
      }
    };
    this.__preparedStmtOfDeleteAllStories = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM story";
        return _query;
      }
    };
  }

  @Override
  public Long insertStory(final Story story) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      long _result = __insertionAdapterOfStory.insertAndReturnId(story);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteStory(final Story story) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfStory.handle(story);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateStory(final Story story) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfStory.handle(story);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAllStories() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllStories.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAllStories.release(_stmt);
    }
  }

  @Override
  public LiveData<List<Story>> getAllStories() {
    final String _sql = "SELECT * FROM story";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[]{"story"}, false, new Callable<List<Story>>() {
      @Override
      public List<Story> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfBody = CursorUtil.getColumnIndexOrThrow(_cursor, "body");
          final int _cursorIndexOfCategoryId = CursorUtil.getColumnIndexOrThrow(_cursor, "categoryId");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "imageUrl");
          final int _cursorIndexOfImageName = CursorUtil.getColumnIndexOrThrow(_cursor, "imageName");
          final int _cursorIndexOfAge = CursorUtil.getColumnIndexOrThrow(_cursor, "age");
          final int _cursorIndexOfAuthor = CursorUtil.getColumnIndexOrThrow(_cursor, "author");
          final int _cursorIndexOfStoryDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "storyDuration");
          final int _cursorIndexOfIsPremium = CursorUtil.getColumnIndexOrThrow(_cursor, "isPremium");
          final int _cursorIndexOfLikesCount = CursorUtil.getColumnIndexOrThrow(_cursor, "likesCount");
          final int _cursorIndexOfDislikesCount = CursorUtil.getColumnIndexOrThrow(_cursor, "dislikesCount");
          final int _cursorIndexOfReaction = CursorUtil.getColumnIndexOrThrow(_cursor, "reaction");
          final int _cursorIndexOfBookmark = CursorUtil.getColumnIndexOrThrow(_cursor, "bookmark");
          final int _cursorIndexOfLiked = CursorUtil.getColumnIndexOrThrow(_cursor, "liked");
          final int _cursorIndexOfDisliked = CursorUtil.getColumnIndexOrThrow(_cursor, "disliked");
          final List<Story> _result = new ArrayList<Story>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Story _item;
            _item = new Story();
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            _item.setId(_tmpId);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            _item.setTitle(_tmpTitle);
            final String _tmpBody;
            _tmpBody = _cursor.getString(_cursorIndexOfBody);
            _item.setBody(_tmpBody);
            final int _tmpCategoryId;
            _tmpCategoryId = _cursor.getInt(_cursorIndexOfCategoryId);
            _item.setCategoryId(_tmpCategoryId);
            final int _tmpUserId;
            _tmpUserId = _cursor.getInt(_cursorIndexOfUserId);
            _item.setUserId(_tmpUserId);
            final String _tmpImageUrl;
            _tmpImageUrl = _cursor.getString(_cursorIndexOfImageUrl);
            _item.setImageUrl(_tmpImageUrl);
            final String _tmpImageName;
            _tmpImageName = _cursor.getString(_cursorIndexOfImageName);
            _item.setImageName(_tmpImageName);
            final String _tmpAge;
            _tmpAge = _cursor.getString(_cursorIndexOfAge);
            _item.setAge(_tmpAge);
            final String _tmpAuthor;
            _tmpAuthor = _cursor.getString(_cursorIndexOfAuthor);
            _item.setAuthor(_tmpAuthor);
            final String _tmpStoryDuration;
            _tmpStoryDuration = _cursor.getString(_cursorIndexOfStoryDuration);
            _item.setStoryDuration(_tmpStoryDuration);
            final int _tmpIsPremium;
            _tmpIsPremium = _cursor.getInt(_cursorIndexOfIsPremium);
            _item.setIsPremium(_tmpIsPremium);
            final int _tmpLikesCount;
            _tmpLikesCount = _cursor.getInt(_cursorIndexOfLikesCount);
            _item.setLikesCount(_tmpLikesCount);
            final int _tmpDislikesCount;
            _tmpDislikesCount = _cursor.getInt(_cursorIndexOfDislikesCount);
            _item.setDislikesCount(_tmpDislikesCount);
            final String _tmpReaction;
            _tmpReaction = _cursor.getString(_cursorIndexOfReaction);
            _item.setReaction(_tmpReaction);
            final boolean _tmpBookmark;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfBookmark);
            _tmpBookmark = _tmp != 0;
            _item.setBookmark(_tmpBookmark);
            final boolean _tmpLiked;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfLiked);
            _tmpLiked = _tmp_1 != 0;
            _item.setLiked(_tmpLiked);
            final boolean _tmpDisliked;
            final int _tmp_2;
            _tmp_2 = _cursor.getInt(_cursorIndexOfDisliked);
            _tmpDisliked = _tmp_2 != 0;
            _item.setDisliked(_tmpDisliked);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }
}
