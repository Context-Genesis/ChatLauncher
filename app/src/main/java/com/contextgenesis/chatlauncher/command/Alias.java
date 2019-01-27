package com.contextgenesis.chatlauncher.command;

import lombok.Getter;

@Getter
public class Alias {

    private String aliasName;
    private String command;

    public Alias(String aliasName, String command) {
        this.aliasName = aliasName;
        this.command = command;
    }

    @Override
    public boolean equals(Object o) {
        Alias aliasObj = (Alias) o;
        return aliasName.equals(aliasObj.aliasName);
    }

    @Override
    public int hashCode() {
        return aliasName.hashCode();
    }

}