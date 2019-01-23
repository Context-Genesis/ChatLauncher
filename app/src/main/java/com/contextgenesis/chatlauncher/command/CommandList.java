package com.contextgenesis.chatlauncher.command;

/**
 * A static access to all the commands we accept
 * FIXME: I'm not really sure I'm too happy with this, but idk.
 * Should we inject this list?
 */
public final class CommandList {

    public static final Command[] commands = new Command[]{
            new AppLaunchCommand()
    };

    private CommandList() {
    }

}

