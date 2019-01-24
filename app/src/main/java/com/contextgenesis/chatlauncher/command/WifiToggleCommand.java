package com.contextgenesis.chatlauncher.command;

public class WifiToggleCommand implements Command {

    @Override
    public Type getType() {
        return Type.WIFI_TOGGLE;
    }

    @Override
    public String getName() {
        return "wifi";
    }

    @Override
    public String getHelpText() {
        return "something went wrong";
    }
}
