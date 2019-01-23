package com.contextgenesis.chatlauncher.command;

import lombok.Getter;

public interface Command {

    Type getType();

    /**
     * Name of the command
     */
    String getName();

    /**
     * Text to print happens when a command is entered wrong
     */
    String getHelpText();

    enum Type {

        UNKNOWN(-1),
        LAUNCH_APP(0),
        CALL(1);

        @Getter
        private final int id;

        Type(int id) {
            this.id = id;
        }
    }
}