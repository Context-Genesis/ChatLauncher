package com.contextgenesis.chatlauncher.manager.input;

import com.contextgenesis.chatlauncher.command.CommandType;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import lombok.Getter;

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

    private static Object[] getArgs(String input) {
        String[] split = input.split(" ");
        if (split.length == 0) {
            return new Object[0];
        } else {
            return Arrays.copyOfRange(split, 1, split.length);
        }
    }

    /**
     * @param text input text
     * @param n    index from the input text
     * @return nth item from the input text split by space
     */
    @Nullable
    private static String getNthString(String text, int n) {
        if (text == null || n < 0) {
            return null;
        }
        String[] split = text.split(" ");
        if (split.length == 0) {
            return text;
        } else if (n >= split.length) {
            return null;
        } else {
            return split[n];
        }
    }
}
