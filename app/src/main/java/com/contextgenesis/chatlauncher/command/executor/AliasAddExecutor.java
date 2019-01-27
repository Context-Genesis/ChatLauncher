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
        aliasManager.addAlias(args[0].trim(), args[1].trim());
    }
}
