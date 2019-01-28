package com.contextgenesis.chatlauncher.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class InputMessageEvent extends Event {

    private final String message;
    /**
     * This will be caught by the Activity and only set the EditText to contain this message.
     */
    private final boolean needsMoreInput;

}
