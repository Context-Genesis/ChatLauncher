package com.contextgenesis.chatlauncher.command.executor;

import com.contextgenesis.chatlauncher.manager.call.CallManager;

import javax.inject.Inject;

public class CallExecutor extends CommandExecutor {

    @Inject
    CallManager callManager;

    @Inject
    public CallExecutor() {
    }

    @Override
    public String execute() {
        if (inputMessage.getArgs().length <= 0) {
            return "Tell me who you want to call!";
        }

        String input = (String) inputMessage.getArgs()[0];
        if (callManager.call(input)) {
            return "Calling " + input;
        } else {
            return "Unable to call";
        }
    }
}
