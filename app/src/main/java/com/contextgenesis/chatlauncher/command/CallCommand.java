package com.contextgenesis.chatlauncher.command;

public class CallCommand implements Command {

    @Override
    public Type getType() {
        return Type.CALL;
    }

    @Override
    public String getName() {
        return "call";
    }

    @Override
    public ArgInfo[] getArgs() {
        return new ArgInfo[]{
                new ArgInfo(true, "", ArgInfo.Type.CONTACTS)
        };
    }

    @Override
    public String getHelpText() {
        return "enter the name of the person you'd like to call";
    }
}
