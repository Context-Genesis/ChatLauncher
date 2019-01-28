package com.contextgenesis.chatlauncher.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OutputMessageEvent extends Event {

    private final String message;
}
