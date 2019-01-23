package com.contextgenesis.chatlauncher.command.executor;

import android.content.Context;

import com.contextgenesis.chatlauncher.manager.app.AppManager;

import javax.inject.Inject;

public class AppLaunchExecutor extends CommandExecutor {

    @Inject
    AppManager appManager;
    @Inject
    Context context;

    @Inject
    public AppLaunchExecutor() {
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
