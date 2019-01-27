package com.contextgenesis.chatlauncher.command;

import lombok.Getter;

@Getter
public class Alias {

    private final String aliasName;
    private final String command;

    public Alias(String aliasName, String command) {
        this.aliasName = aliasName;
        this.command = command;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Alias)) {
            return false;
        }
        Alias aliasObj = (Alias) o;
        return aliasName.equals(aliasObj.aliasName);
    }

    @Override
    public int hashCode() {
        return aliasName.hashCode();
    }

}