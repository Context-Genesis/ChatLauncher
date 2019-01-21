package com.contextgenesis.chatlauncher.manager.input;

import com.contextgenesis.chatlauncher.command.AppLaunchCommand;
import com.contextgenesis.chatlauncher.command.Command;

import org.apache.commons.lang3.StringUtils;

/**
 * Parses the input typed in the by the user and further processes it and send it to the various
 * managers
 */
public class InputManager {

    public String executeInput(String input) {
        Command command = parseInput(input);
        if (command == null) {
            return "So, here's the thing. I'm sort of dumb right now.";
        }
        switch (command.getType()) {
            case LAUNCH_APP:
                return command.execute();
            default:
                return "So, here's the thing. I'm sort of dumb right now.";
        }
    }

    private Command parseInput(String input) {
        input = StringUtils.trim(input);
        switch (getNthString(input, 0)) {
            case "open":
                return new AppLaunchCommand();
        }
        return null;
    }

    /**
     * @param text input text
     * @param n    index from the input text
     * @return nth item from the input text split by space
     */
    private String getNthString(String text, int n) {
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
