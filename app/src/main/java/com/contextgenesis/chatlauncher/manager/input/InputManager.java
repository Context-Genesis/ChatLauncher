package com.contextgenesis.chatlauncher.manager.input;

import com.contextgenesis.chatlauncher.command.AppLaunchCommand;

/**
 * Parses the input typed in the by the user and further processes it and send it to the various
 * managers
 */
public class InputManager {

    public String executeInput(String input) {
        InputMessage inputMessage = new InputMessage(input);
        switch (inputMessage.getCommandType()) {
            case LAUNCH_APP:
                return new AppLaunchCommand(inputMessage).execute();
            default:
                return "So, here's the thing. I'm sort of dumb right now.";
        }
    }

}
