package com.contextgenesis.chatlauncher.manager.input;

import com.contextgenesis.chatlauncher.command.Command;
import com.contextgenesis.chatlauncher.command.CommandList;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

import androidx.annotation.NonNull;
import lombok.Getter;

import static com.contextgenesis.chatlauncher.utils.StringUtils.getNthString;

@Getter
public class InputMessage {

    private final String inputMessage;
    private final Command.Type commandType;
    private final Object[] args;
    private boolean isValid;

    public InputMessage(String inputMessage) {
        this.inputMessage = inputMessage;
        commandType = getCommandTypeFromInput(inputMessage);
        args = getArgsFromInput(inputMessage);
        isValid = true;
    }

    public static InputMessage invalidMessage(String inputMessage, Command.Type commandType) {
        InputMessage msg = new InputMessage(inputMessage);
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
