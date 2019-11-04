package com.project.android_kidstories.DataStore;

import androidx.room.*;
import com.project.android_kidstories.data.model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    Long insertUser(User user);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("SELECT * FROM user")
    List<User> getallUsers();
}
