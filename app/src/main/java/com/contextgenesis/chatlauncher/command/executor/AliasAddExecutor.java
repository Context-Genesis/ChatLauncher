package com.contextgenesis.chatlauncher.command.executor;

import com.contextgenesis.chatlauncher.manager.alias.AliasManager;

import javax.inject.Inject;

public class AliasAddExecutor extends CommandExecutor {

    @Inject
    AliasManager aliasManager;

    @Inject
    public AliasAddExecutor() {
    }

    @Override
    public void execute() {
        String args[] = (String[]) inputMessage.getArgs();
        String input[] = args[0].split("=");
        aliasManager.addAlias(input[0].trim(), input[1].trim());
    }
}
