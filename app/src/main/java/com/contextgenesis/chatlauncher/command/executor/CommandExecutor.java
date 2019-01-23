package com.contextgenesis.chatlauncher.command.executor;

import com.contextgenesis.chatlauncher.manager.input.InputMessage;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class CommandExecutor {

    InputMessage inputMessage;

    public abstract String execute();

}
