package com.contextgenesis.chatlauncher.database.dao;

import com.contextgenesis.chatlauncher.database.entity.AliasEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface AliasDao {

    @Insert
    void insert(AliasEntity aliasEntity);

    @Query("DELETE FROM alias WHERE name = :aliasName")
    void deleteByAliasName(String aliasName);

    @Query("SELECT * from alias")
    List<AliasEntity> getAllAlias();

}