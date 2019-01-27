package com.contextgenesis.chatlauncher.command.executor;

import com.contextgenesis.chatlauncher.manager.alias.AliasManager;

import javax.inject.Inject;

public class AliasRemoveExecutor extends CommandExecutor {

    @Inject
    AliasManager aliasManager;

    @Inject
    public AliasRemoveExecutor() {
    }

    @Override
    public void execute() {
        String args[] = (String[]) inputMessage.getArgs();
        aliasManager.removeAlias(args[0].trim());
    }
}
