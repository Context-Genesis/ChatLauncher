package com.contextgenesis.chatlauncher.fluidresize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class KeyboardVisibilityChanged {
    private boolean visible;
    private int contentHeight;
    private int contentHeightBeforeResize;
}
