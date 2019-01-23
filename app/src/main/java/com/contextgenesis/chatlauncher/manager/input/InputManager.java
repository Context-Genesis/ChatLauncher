package com.contextgenesis.chatlauncher.manager.input;

import com.contextgenesis.chatlauncher.command.executor.AppLaunchExecutor;

import javax.inject.Inject;

/**
 * Parses the input typed in the by the user and further processes it and send it to the various
 * managers
 */
public class InputManager {

    @Inject
    AppLaunchExecutor appLaunchExecutor;

    @Inject
    public InputManager() {
    }

    public String executeInput(String input) {
        InputMessage inputMessage = new InputMessage(input);
        switch (inputMessage.getCommandType()) {
            case LAUNCH_APP:
                appLaunchExecutor.setInputMessage(inputMessage);
                return appLaunchExecutor.execute();
            default:
                return "So, here's the thing. I'm sort of dumb right now.";
        }
    }

}
