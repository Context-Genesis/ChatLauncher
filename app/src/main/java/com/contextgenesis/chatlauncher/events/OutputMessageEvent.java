package com.contextgenesis.chatlauncher.events;

import lombok.Getter;

@Getter
public class OutputMessageEvent extends Event {

    private final String message;

    public OutputMessageEvent(String message) {
        this.message = message;
    }
}
