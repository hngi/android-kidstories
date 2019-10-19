package com.project.android_kidstories.DataStore;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class StoryDatabase_Impl extends StoryDatabase {
  private volatile StoryDao _storyDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(4) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Story` (`id` INTEGER NOT NULL, `title` TEXT, `body` TEXT, `categoryId` INTEGER NOT NULL, `userId` INTEGER NOT NULL, `imageUrl` TEXT, `imageName` TEXT, `age` TEXT, `author` TEXT, `storyDuration` TEXT, `isPremium` INTEGER NOT NULL, `likesCount` INTEGER NOT NULL, `dislikesCount` INTEGER NOT NULL, `reaction` TEXT, `bookmark` INTEGER NOT NULL, `liked` INTEGER NOT NULL, `disliked` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `user` (`token` TEXT NOT NULL, `name` TEXT, `firstName` TEXT, `lastName` TEXT, `email` TEXT, `admin` INTEGER, `premium` INTEGER, `id` INTEGER, `bookmarkCount` INTEGER, `liked` INTEGER, `image` TEXT, `phoneNumber` TEXT, `designation` TEXT, `password` TEXT, `photo` TEXT, `title` TEXT, `role` TEXT, PRIMARY KEY(`token`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '31ce02357f1ce67a3e5eac2fea890611')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `Story`");
        _db.execSQL("DROP TABLE IF EXISTS `user`");
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsStory = new HashMap<String, TableInfo.Column>(17);
        _columnsStory.put("id", new TableInfo.Column("id", "INTEGER", true, 1));
        _columnsStory.put("title", new TableInfo.Column("title", "TEXT", false, 0));
        _columnsStory.put("body", new TableInfo.Column("body", "TEXT", false, 0));
        _columnsStory.put("categoryId", new TableInfo.Column("categoryId", "INTEGER", true, 0));
        _columnsStory.put("userId", new TableInfo.Column("userId", "INTEGER", true, 0));
        _columnsStory.put("imageUrl", new TableInfo.Column("imageUrl", "TEXT", false, 0));
        _columnsStory.put("imageName", new TableInfo.Column("imageName", "TEXT", false, 0));
        _columnsStory.put("age", new TableInfo.Column("age", "TEXT", false, 0));
        _columnsStory.put("author", new TableInfo.Column("author", "TEXT", false, 0));
        _columnsStory.put("storyDuration", new TableInfo.Column("storyDuration", "TEXT", false, 0));
        _columnsStory.put("isPremium", new TableInfo.Column("isPremium", "INTEGER", true, 0));
        _columnsStory.put("likesCount", new TableInfo.Column("likesCount", "INTEGER", true, 0));
        _columnsStory.put("dislikesCount", new TableInfo.Column("dislikesCount", "INTEGER", true, 0));
        _columnsStory.put("reaction", new TableInfo.Column("reaction", "TEXT", false, 0));
        _columnsStory.put("bookmark", new TableInfo.Column("bookmark", "INTEGER", true, 0));
        _columnsStory.put("liked", new TableInfo.Column("liked", "INTEGER", true, 0));
        _columnsStory.put("disliked", new TableInfo.Column("disliked", "INTEGER", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysStory = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesStory = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoStory = new TableInfo("Story", _columnsStory, _foreignKeysStory, _indicesStory);
        final TableInfo _existingStory = TableInfo.read(_db, "Story");
        if (! _infoStory.equals(_existingStory)) {
          throw new IllegalStateException("Migration didn't properly handle Story(com.project.android_kidstories.Model.Story).\n"
                  + " Expected:\n" + _infoStory + "\n"
                  + " Found:\n" + _existingStory);
        }
        final HashMap<String, TableInfo.Column> _columnsUser = new HashMap<String, TableInfo.Column>(17);
        _columnsUser.put("token", new TableInfo.Column("token", "TEXT", true, 1));
        _columnsUser.put("name", new TableInfo.Column("name", "TEXT", false, 0));
        _columnsUser.put("firstName", new TableInfo.Column("firstName", "TEXT", false, 0));
        _columnsUser.put("lastName", new TableInfo.Column("lastName", "TEXT", false, 0));
        _columnsUser.put("email", new TableInfo.Column("email", "TEXT", false, 0));
        _columnsUser.put("admin", new TableInfo.Column("admin", "INTEGER", false, 0));
        _columnsUser.put("premium", new TableInfo.Column("premium", "INTEGER", false, 0));
        _columnsUser.put("id", new TableInfo.Column("id", "INTEGER", false, 0));
        _columnsUser.put("bookmarkCount", new TableInfo.Column("bookmarkCount", "INTEGER", false, 0));
        _columnsUser.put("liked", new TableInfo.Column("liked", "INTEGER", false, 0));
        _columnsUser.put("image", new TableInfo.Column("image", "TEXT", false, 0));
        _columnsUser.put("phoneNumber", new TableInfo.Column("phoneNumber", "TEXT", false, 0));
        _columnsUser.put("designation", new TableInfo.Column("designation", "TEXT", false, 0));
        _columnsUser.put("password", new TableInfo.Column("password", "TEXT", false, 0));
        _columnsUser.put("photo", new TableInfo.Column("photo", "TEXT", false, 0));
        _columnsUser.put("title", new TableInfo.Column("title", "TEXT", false, 0));
        _columnsUser.put("role", new TableInfo.Column("role", "TEXT", false, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUser = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUser = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUser = new TableInfo("user", _columnsUser, _foreignKeysUser, _indicesUser);
        final TableInfo _existingUser = TableInfo.read(_db, "user");
        if (! _infoUser.equals(_existingUser)) {
          throw new IllegalStateException("Migration didn't properly handle user(com.project.android_kidstories.Model.User).\n"
                  + " Expected:\n" + _infoUser + "\n"
                  + " Found:\n" + _existingUser);
        }
      }
    }, "31ce02357f1ce67a3e5eac2fea890611", "920c77e982ccfd936cb56fa61113f54c");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "Story","user");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `Story`");
      _db.execSQL("DELETE FROM `user`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public StoryDao storyDao() {
    if (_storyDao != null) {
      return _storyDao;
    } else {
      synchronized(this) {
        if(_storyDao == null) {
          _storyDao = new StoryDao_Impl(this);
        }
        return _storyDao;
      }
    }
  }
}
