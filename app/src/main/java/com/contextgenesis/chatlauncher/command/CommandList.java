package com.contextgenesis.chatlauncher.command;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class CommandList {

    private final List<Command> commands;

    public CommandList() {
        this.commands = new ArrayList<>();
        this.commands.add(new AppLaunchCommand());
    }

}

