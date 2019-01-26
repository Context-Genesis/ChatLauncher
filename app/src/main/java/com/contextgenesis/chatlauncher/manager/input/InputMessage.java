package com.contextgenesis.chatlauncher.manager.input;

import com.contextgenesis.chatlauncher.command.Command;

import lombok.Getter;

/**
 * An instance of this class can only be created by InputParser.
 * This guarantees that the incoming data is valid
 */
@Getter
public final class InputMessage {

    private final String inputMessage;
    private final Command.Type commandType;
    private String[] args;
    private boolean isValid;

    private InputMessage(String inputMessage, Command.Type commandType) {
        this.inputMessage = inputMessage;
        this.commandType = commandType;
    }

    @SuppressWarnings("PMD.UseVarargs")
    public static InputMessage validMessage(String inputMessage, Command.Type commandType, String[] args) {
        InputMessage msg = new InputMessage(inputMessage, commandType);
        msg.isValid = true;
        msg.args = args;
        return msg;
    }

    public static InputMessage invalidMessage(String inputMessage, Command.Type commandType) {
        InputMessage msg = new InputMessage(inputMessage, commandType);
        msg.isValid = false;
        return msg;
    }
}
