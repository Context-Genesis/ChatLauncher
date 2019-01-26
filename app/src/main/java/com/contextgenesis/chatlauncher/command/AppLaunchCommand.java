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
    public ArgInfo[] getArgs() {
        return new ArgInfo[]{
                new ArgInfo(true, "", ArgInfo.Type.APPS)
        };
    }

    @Override
    public String getHelpText() {
        return "enter the name of the app to open it";
    }
}
