package com.contextgenesis.chatlauncher.database.entity;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "alias")
public class AliasEntity implements Serializable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "name")
    private final String aliasName;

    @ColumnInfo(name = "command")
    private final String command;

    public AliasEntity(@NonNull String aliasName, String command) {
        this.aliasName = aliasName;
        this.command = command;
    }

    @NonNull
    public String getAliasName() {
        return aliasName;
    }

    public String getCommand() {
        return command;
    }

}