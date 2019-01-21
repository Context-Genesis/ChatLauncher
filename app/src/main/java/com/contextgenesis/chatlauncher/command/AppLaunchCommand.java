package com.contextgenesis.chatlauncher.command;

public class AppLaunchCommand implements Command {

    @Override
    public Type getType() {
        return Type.LAUNCH_APP;
    }

    @Override
    public String getName() {
        return "launch";
    }

    @Override
    public String getHelpText() {
        return "enter the name of the app to open it";
    }
}
