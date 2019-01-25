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
    public void execute() {
        if (inputMessage.getArgs().length <= 0) {
            postOutput("Tell me what you want to launch!");
        } else {
            String appName = (String) inputMessage.getArgs()[0];
            if (appManager.launchApp(context, appManager.getAppInfoFromName(appName))) {
                postOutput(String.format("Launching %s", appName));
            } else {
                postOutput(String.format("Unable to open %s. Who knows why?", appName));
            }
        }
    }
}
