package com.contextgenesis.chatlauncher.manager.input;

import com.contextgenesis.chatlauncher.command.CommandType;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

import androidx.annotation.NonNull;
import lombok.Getter;

import static com.contextgenesis.chatlauncher.utils.StringUtils.getNthString;

@Getter
public class InputMessage {

    private final String inputMessage;
    private final CommandType commandType;
    private final Object[] args;

    public InputMessage(String inputMessage) {
        this.inputMessage = inputMessage;
        commandType = getCommandType(inputMessage);
        args = getArgs(inputMessage);
    }

    @NonNull
    private static CommandType getCommandType(String input) {
        String trimmedInput = StringUtils.trim(input);
        switch (getNthString(trimmedInput, 0).toLowerCase()) {
            case "launch":
                return CommandType.LAUNCH_APP;
            default:
                return CommandType.UNKNOWN;
        }
    }

    /**
     * @param input input message
     * @return arguments; array split by spaces not including the first word
     */
    private static Object[] getArgs(String input) {
        String[] split = input.split(" ");
        if (split.length == 0) {
            return new Object[0];
        } else {
            return Arrays.copyOfRange(split, 1, split.length);
        }
    }
}
