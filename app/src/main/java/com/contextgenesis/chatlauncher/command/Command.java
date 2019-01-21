package com.contextgenesis.chatlauncher.command;

public interface Command {

    CommandType getType();

    /**
     * Name of the command
     */
    String getName();

    /**
     * Text to print happens when a command is entered wrong
     */
    String getHelpText();

    String execute();

}
