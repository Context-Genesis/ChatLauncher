package com.contextgenesis.chatlauncher.manager.alias;

import com.contextgenesis.chatlauncher.command.Alias;
import com.contextgenesis.chatlauncher.database.entity.AliasEntity;
import com.contextgenesis.chatlauncher.database.repository.AliasRepository;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AliasManager {

    private final Set<Alias> aliasSet;

    AliasRepository aliasRepository;

    @Inject
    public AliasManager(AliasRepository aliasRepository) {
        aliasSet = new HashSet<>();
        this.aliasRepository = aliasRepository;
        aliasRepository.getAllAlias().subscribe(aliasEntities -> {
            for (AliasEntity aliasEntity : aliasEntities) {
                aliasSet.add(new Alias(aliasEntity.getAliasName(), aliasEntity.getCommand()));
            }
        });
    }

    public String addAlias(String alias, String command) {
        Alias newAlias = new Alias(alias, command);
        aliasRepository.insert(alias, command);
        aliasSet.add(newAlias);
        return "Alias add successful";
    }

    public String removeAlias(String aliasName) {
        Alias rmAlias = getAlias(aliasName);
        aliasRepository.delete(aliasName);
        aliasSet.remove(rmAlias);
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
