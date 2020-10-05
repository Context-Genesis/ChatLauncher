package com.contextgenesis.chatlauncher.database;

import com.contextgenesis.chatlauncher.database.dao.AliasDao;
import com.contextgenesis.chatlauncher.database.dao.SuggestDao;
import com.contextgenesis.chatlauncher.database.entity.AliasEntity;
import com.contextgenesis.chatlauncher.database.entity.SuggestEntity;

import javax.inject.Singleton;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Singleton
@Database(entities = {AliasEntity.class, SuggestEntity.class}, version = 1, exportSchema = false)
public abstract class LauncherDatabase extends RoomDatabase {

    public abstract AliasDao aliasDao();

    public abstract SuggestDao suggestDao();
}