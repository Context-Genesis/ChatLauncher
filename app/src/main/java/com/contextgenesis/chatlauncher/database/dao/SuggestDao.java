package com.contextgenesis.chatlauncher.database.dao;

import com.contextgenesis.chatlauncher.database.entity.SuggestEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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

    @Query("SELECT * from suggestion WHERE argType=:argType AND name Like  '%' || :input || '%' Order By clickCount DESC")
    List<SuggestEntity> getSuggestions(String input, int argType);

    @Query("SELECT * from suggestion WHERE argType=:argType AND name Like  '%' || :input || '%' AND name IN (:predefinedInputs) Order By name")
    List<SuggestEntity> getPredefinedInputs(String input, String[] predefinedInputs, int argType);

}