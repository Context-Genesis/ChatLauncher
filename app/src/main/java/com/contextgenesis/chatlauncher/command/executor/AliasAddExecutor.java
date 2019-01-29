package com.contextgenesis.chatlauncher.command.executor;

import com.contextgenesis.chatlauncher.manager.alias.AliasManager;

import javax.inject.Inject;

public class AliasAddExecutor extends CommandExecutor {

    @Inject
    AliasManager aliasManager;

    @Inject
    public AliasAddExecutor() {
    }

    /**
     * todo: make the aliasManager return whether the add call was successful and then return
     * the result via postOutput from here.
     */
    @Override
    public void execute() {
        String args[] = (String[]) inputMessage.getArgs();
        aliasManager.addAlias(args[0].trim(), args[1].trim());
    }
}
