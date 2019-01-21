package com.contextgenesis.chatlauncher.command;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class CommandList {

    private static CommandList instance;
    private final List<Command> commands;

    private CommandList() {
        this.commands = new ArrayList<>();
        this.commands.add(new AppLaunchCommand());
    }

    public static CommandList get() {
        if (instance == null) {
            instance = new CommandList();
        }
        return instance;
    }
}

