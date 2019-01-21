package com.contextgenesis.chatlauncher.command.executor;

import android.content.Context;

import com.contextgenesis.chatlauncher.RootProviders;
import com.contextgenesis.chatlauncher.manager.app.AppManager;
import com.contextgenesis.chatlauncher.manager.input.InputMessage;

public class AppLaunchExecutor extends CommandExecutor {

    private final AppManager appManager;
    private final Context context;
    private final InputMessage inputMessage;

    public AppLaunchExecutor(InputMessage inputMessage) {
        super(inputMessage);
        this.inputMessage = inputMessage;
        context = RootProviders.get().getContext();
        appManager = RootProviders.get().getAppManager();
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
