package com.contextgenesis.chatlauncher.command;

import lombok.Getter;

public enum CommandType {

    LAUNCH_APP(0),
    CALL(1);

    @Getter
    private final int id;

    CommandType(int id) {
        this.id = id;
    }
}
