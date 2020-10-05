package com.contextgenesis.chatlauncher.command;

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
        BLUETOOTH_TOGGLE(3),
        ALIAS_ADD(4),
        ALIAS_REMOVE(5);

        @Getter
        private final int id;

        Type(int id) {
            this.id = id;
        }
    }

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
        private final String[] predefinedInputs;

        public ArgInfo(boolean isRequired, String identifier, Type type) {
            this.isRequired = isRequired;
            this.identifier = identifier;
            this.type = type;
            this.predefinedInputs = new String[0];
        }

        @SuppressWarnings("PMD.UseVarargs")
        public ArgInfo(boolean isRequired, String identifier, Type type, String[] predefinedInputs) {
            this.isRequired = isRequired;
            this.identifier = identifier;
            this.type = type;
            this.predefinedInputs = predefinedInputs.clone();
        }

        public boolean hasPredefinedInputs() {
            if (predefinedInputs.length > 0)
                return true;
            return false;
        }

        public String[] getPredefinedInputs() {
            return predefinedInputs;
        }

        public enum Type {
            APPS(0),
            CONTACTS(1),
            PREDEFINED(2),
            NO_SUGGEST(3),
            ALIAS(4),
            COMMAND(5);

            @Getter
            private final int id;

            Type(int id) {
                this.id = id;
            }
        }
    }
}
