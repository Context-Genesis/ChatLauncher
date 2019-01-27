package com.contextgenesis.chatlauncher.command;

public class AliasAddCommand implements Command {

    @Override
    public Type getType() {
        return Type.ALIAS_ADD;
    }

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public ArgInfo[] getArgs() {
        return new ArgInfo[]{
                new ArgInfo(true, "", ArgInfo.Type.ALIAS_ADD),
                new ArgInfo(true, "=", ArgInfo.Type.ALIAS_ADD)
        };
    }

    @Override
    public String getHelpText() {
        return "Add alias for any app";
    }
}