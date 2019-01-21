package com.contextgenesis.chatlauncher.command;

import android.content.Context;

import com.contextgenesis.chatlauncher.RootProviders;
import com.contextgenesis.chatlauncher.manager.app.AppManager;

public class AppLaunchCommand implements Command {

    private final String arg;
    private final AppManager appManager;
    private final Context context;

    public AppLaunchCommand(String arg) {
        this.arg = arg;
        context = RootProviders.get().getContext();
        appManager = RootProviders.get().getAppManager();
    }

    @Override
    public CommandType getType() {
        return CommandType.LAUNCH_APP;
    }

    @Override
    public String getName() {
        return "open";
    }

    @Override
    public String getHelpText() {
        return "enter the name of the app to open it";
    }

    @Override
    public String execute() {
        if (appManager.launchApp(context, appManager.getAppInfoFromName(arg))) {
            return "Hooray the app launch command has executed";
        } else {
            return "Unable to execute the command";
        }
    }
}
