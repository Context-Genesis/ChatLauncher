package com.contextgenesis.chatlauncher.database.entity;

import java.io.Serializable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "alias")
public class AliasEntity implements Serializable {

    public AliasEntity(@NonNull String aliasName, String command) {
        this.aliasName = aliasName;
        this.command = command;
    }

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "name")
    private String aliasName;

    @ColumnInfo(name = "command")
    private String command;

    @NonNull
    public String getAliasName() {
        return aliasName;
    }

    public String getCommand() {
        return command;
    }

}