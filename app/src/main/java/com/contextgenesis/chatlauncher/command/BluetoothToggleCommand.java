package com.contextgenesis.chatlauncher.command;

public class BluetoothToggleCommand implements Command {

    @Override
    public Type getType() {
        return Type.BLUETOOTH_TOGGLE;
    }

    @Override
    public String getName() {
        return "bluetooth";
    }

    @Override
    public String getHelpText() {
        return "something went wrong";
    }
}
