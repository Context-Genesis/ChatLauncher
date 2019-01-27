package com.contextgenesis.chatlauncher.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * A static access to all the commands we accept
 * FIXME: I'm not really sure I'm too happy with this, but idk.
 * Should we inject this list?
 */
public final class CommandList {

    private static final Command[] commands = new Command[]{
            new AppLaunchCommand(),
            new WifiToggleCommand(),
            new BluetoothToggleCommand(),
            new CallCommand(),
            new AliasAddCommand(),
            new AliasRemoveCommand()
    };

    public static final List<Command> COMMANDS =
            Collections.unmodifiableList(Arrays.asList(commands));

    private CommandList() {
    }

    @Nullable
    public static Command get(Command.Type type) {
        for (Command command : COMMANDS) {
            if (command.getType() == type) {
                return command;
            }
        }
        return null;
    }

}

