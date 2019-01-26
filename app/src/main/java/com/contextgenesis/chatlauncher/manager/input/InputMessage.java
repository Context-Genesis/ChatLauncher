package com.contextgenesis.chatlauncher.manager.input;

import com.contextgenesis.chatlauncher.command.Command;
import com.contextgenesis.chatlauncher.command.CommandList;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

import androidx.annotation.NonNull;
import lombok.Getter;

import static com.contextgenesis.chatlauncher.utils.StringUtils.getNthString;

/**
 * An instance of this class can only be created by InputParser.
 * This guarantees that the incoming data is valid
 */
@Getter
public class InputMessage {

    private final String inputMessage;
    private final Command.Type commandType;
    private String[] args;
    private boolean isValid;

    private InputMessage(String inputMessage, Command.Type commandType) {
        this.inputMessage = inputMessage;
        this.commandType = commandType;
    }

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

    @NonNull
    private static Command.Type getCommandTypeFromInput(String input) {
        String trimmedInput = StringUtils.trim(input);
        String commandString = getNthString(trimmedInput, 0).toLowerCase();
        for (Command command : CommandList.COMMANDS) {
            if (command.getName().equalsIgnoreCase(commandString)) {
                return command.getType();
            }
        }
        return Command.Type.UNKNOWN;
    }

    /**
     * @param input input message
     * @return arguments; array split by spaces not including the first word
     */
    private static Object[] getArgsFromInput(String input) {
        String[] split = input.split(" ");
        if (split.length == 0) {
            return new Object[0];
        } else {
            return Arrays.copyOfRange(split, 1, split.length);
        }
    }
}
