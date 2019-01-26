package com.contextgenesis.chatlauncher.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

/***
 * Defines the structure of each command
 * It does not perform any form of data/arguments-content validation; only structural validation
 * */
public interface Command {

    Type getType();

    /**
     * Name of the command
     */
    String getName();

    ArgInfo[] getArgs();

    /**
     * Text to print happens when a command is entered wrong
     */
    String getHelpText();

    enum Type {

        UNKNOWN(-1),
        LAUNCH_APP(0),
        CALL(1),
        WIFI_TOGGLE(2),
        BLUETOOTH_TOGGLE(3);

        @Getter
        private final int id;

        Type(int id) {
            this.id = id;
        }
    }

    @AllArgsConstructor
    @Getter
    class ArgInfo {

        private final boolean isRequired;
        /**
         * The string identifier; for example; alias -set x y.
         * Here, -set is the identifier.
         * <p>
         * Can either be null or an empty string for the first argument that's not required
         */
        private final String identifier;
        private final ArgInfo.Type type;

        public enum Type {
            APPS(0),
            CONTACTS(1),
            PREDEFINED(2);

            @Getter
            private final int id;

            Type(int id) {
                this.id = id;
            }
        }
    }
}
