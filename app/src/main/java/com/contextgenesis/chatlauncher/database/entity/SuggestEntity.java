package com.contextgenesis.chatlauncher.database.entity;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "suggestion")
public class SuggestEntity implements Serializable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "name")
    private final String commandName;

    @ColumnInfo(name = "argType")
    private final int argType;

    @ColumnInfo
    private long clickCount;

    public SuggestEntity(@NonNull String commandName, int argType) {
        this.commandName = commandName;
        this.argType = argType;
        clickCount = 1;
    }

    @NonNull
    public String getCommandName() {
        return commandName;
    }

    public int getArgType() {
        return argType;
    }

    public long getClickCount() {
        return clickCount;
    }

    public void setClickCount(long clickCount) {
        this.clickCount = clickCount;
    }

}