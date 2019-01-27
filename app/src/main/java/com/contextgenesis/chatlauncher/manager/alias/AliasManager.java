package com.contextgenesis.chatlauncher.manager.alias;

import com.contextgenesis.chatlauncher.command.Alias;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AliasManager {

    private Set<Alias> aliasSet;

    // TODO: inject DAO

    @Inject
    public AliasManager() {
        // TODO: load from DB, if first time insert into DB after populating with appNames
        aliasSet = new HashSet<>();
    }

    public String addAlias(String alias, String command) {
        Alias newAlias = new Alias(alias, command);
        aliasSet.add(newAlias);
        // TODO: store it in DB
        return "Alias add successful";
    }

    public String removeAlias(String alias) {
        Alias rmAlias = new Alias(alias, "");
        aliasSet.remove(rmAlias);
        // TODO: db action
        return "Alias remove successful";
    }

    public boolean containsAlias(String alias) {
        return aliasSet.contains(new Alias(alias, ""));
    }

    public Alias getAlias(String aliasName) {
        for (Alias alias : aliasSet) {
            if (alias.getAliasName().equals(aliasName)) {
                return alias;
            }
        }
        return null;
    }

}
