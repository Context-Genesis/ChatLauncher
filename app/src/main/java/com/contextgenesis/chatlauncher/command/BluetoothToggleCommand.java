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
    public ArgInfo[] getArgs() {
        return new ArgInfo[]{
                new ArgInfo(false, "", ArgInfo.Type.PREDEFINED, new String[]{
                        "on",
                        "off"
                })
        };
    }

    @Override
    public String getHelpText() {
        return "something went wrong";
    }
}
