package com.contextgenesis.chatlauncher.command.executor;

import com.contextgenesis.chatlauncher.manager.input.InputMessage;

import lombok.Getter;

@Getter
public abstract class CommandExecutor {

    private final InputMessage inputMessage;

    public CommandExecutor(InputMessage inputMessage) {
        this.inputMessage = inputMessage;
    }

    public abstract String execute();

}
