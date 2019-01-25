package com.contextgenesis.chatlauncher.command.executor;

import com.contextgenesis.chatlauncher.events.OutputMessageEvent;
import com.contextgenesis.chatlauncher.manager.input.InputMessage;
import com.contextgenesis.chatlauncher.rx.RxBus;

import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class CommandExecutor {

    @Inject
    RxBus rxBus;

    InputMessage inputMessage;

    public abstract void execute();

    void postOutput(String output) {
        rxBus.post(new OutputMessageEvent(output));
    }
}
