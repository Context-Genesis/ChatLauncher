package com.contextgenesis.chatlauncher.database;

import com.contextgenesis.chatlauncher.database.dao.AliasDao;
import com.contextgenesis.chatlauncher.database.entity.AliasEntity;

import javax.inject.Singleton;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Singleton
@Database(entities = {AliasEntity.class}, version = 1)
public abstract class AliasDatabase extends RoomDatabase {

    public abstract AliasDao aliasDao();

}