package com.contextgenesis.chatlauncher.database.dao;

import com.contextgenesis.chatlauncher.database.entity.SuggestEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface SuggestDao {

    /*
     insert returns -1 when the data doesn't exist in the table
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(SuggestEntity entity);

    @Query("UPDATE suggestion SET clickCount = clickCount + 1 WHERE name = :command")
    void updateClickCount(String command);

    @Query("SELECT * from suggestion")
    List<SuggestEntity> getAllSuggestions();
}