package com.contextgenesis.chatlauncher.command;

import android.content.Context;

import com.contextgenesis.chatlauncher.RootProviders;
import com.contextgenesis.chatlauncher.manager.app.AppManager;
import com.contextgenesis.chatlauncher.manager.input.InputMessage;

public class AppLaunchCommand implements Command {

    private final AppManager appManager;
    private final Context context;
    private final InputMessage inputMessage;

    public AppLaunchCommand(InputMessage inputMessage) {
        this.inputMessage = inputMessage;
        context = RootProviders.get().getContext();
        appManager = RootProviders.get().getAppManager();
    }

    @Override
    public CommandType getType() {
        return CommandType.LAUNCH_APP;
    }

    @Override
    public String getName() {
        return "launch";
    }

    @Override
    public String getHelpText() {
        return "enter the name of the app to open it";
    }

    @Override
    public String execute() {
        if (inputMessage.getArgs().length <= 0) {
            return "Tell me what you want to launch!";
        }

        String appName = (String) inputMessage.getArgs()[0];
        if (appManager.launchApp(context, appManager.getAppInfoFromName(appName))) {
            return String.format("Launching %s", appName);
        } else {
            return String.format("Unable to open %s. Who knows why?", appName);
        }
    }
}
