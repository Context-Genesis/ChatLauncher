package com.contextgenesis.chatlauncher.command;

public class AliasRemoveCommand implements Command {

    @Override
    public Type getType() {
        return Type.ALIAS_REMOVE;
    }

    @Override
    public String getName() {
        return "unset";
    }

    @Override
    public ArgInfo[] getArgs() {
        return new ArgInfo[]{
                new ArgInfo(true, "", ArgInfo.Type.ALIAS_REMOVE)
        };
    }

    @Override
    public String getHelpText() {
        return "Remove existing alias";
    }
}
