package com.contextgenesis.chatlauncher.command;

public interface Command {

    CommandType getCommandType();

    /**
     * Name of the command
     */
    String name();

    /**
     * Text to print happens when a command is entered wrong
     */
    String helpText();

}
