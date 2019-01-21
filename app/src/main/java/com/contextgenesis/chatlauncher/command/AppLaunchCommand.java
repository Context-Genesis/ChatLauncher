package com.contextgenesis.chatlauncher.command;

public class AppLaunchCommand implements Command {

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
        return "Hooray the app launch command has executed";
    }
}
