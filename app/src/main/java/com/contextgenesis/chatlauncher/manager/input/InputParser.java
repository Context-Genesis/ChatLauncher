package com.contextgenesis.chatlauncher.manager.input;

import com.contextgenesis.chatlauncher.command.Command;
import com.contextgenesis.chatlauncher.command.CommandList;
import com.contextgenesis.chatlauncher.manager.app.AppManager;
import com.contextgenesis.chatlauncher.manager.call.ContactsManager;

import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;

import androidx.annotation.NonNull;

import static com.contextgenesis.chatlauncher.utils.StringUtils.getNthString;

public class InputParser {

    @Inject
    AppManager appManager;
    @Inject
    ContactsManager contactsManager;

    @Inject
    public InputParser() {
    }

    /**
     * This method is responsible for parsing the entire message along with its arguments,
     * and flags whether it is valid or not.
     */
    public InputMessage parse(String input) {
        input = StringUtils.trim(input);
        Command.Type commandType = getCommandTypeFromInput(input);

        // validate command enum
        if (commandType == Command.Type.UNKNOWN) {
            return InputMessage.invalidMessage(input, commandType);
        }

        // todo validate number of args
        String[] args = getArgsFromInput(commandType, input);
        if (args == null) {
            return InputMessage.invalidMessage(input, commandType);
        }

        // check for the data within the args
        Command.ArgInfo[] argsInfo = CommandList.get(commandType).getArgs();
        for (int i = 0; i < argsInfo.length; i++) {
            if (argsInfo[i].isRequired()) {
                switch (argsInfo[i].getType()) {
                    case APPS:
                        if (!appManager.isAppNameValid(args[i])) {
                            return InputMessage.invalidMessage(input, commandType);
                        }
                        break;
                    case CONTACTS:
                        if (!contactsManager.isContactNameValid(args[i])) {
                            return InputMessage.invalidMessage(input, commandType);
                        }
                        break;
                }
            }
        }

        return InputMessage.validMessage(input, commandType, args);
    }

    @NonNull
    private Command.Type getCommandTypeFromInput(String input) {
        String trimmedInput = StringUtils.trim(input);
        String commandString = getNthString(trimmedInput, 0).toLowerCase();
        for (Command command : CommandList.COMMANDS) {
            if (command.getName().equalsIgnoreCase(commandString)) {
                return command.getType();
            }
        }
        return Command.Type.UNKNOWN;
    }

    /**
     * @param input input message
     * @return arguments; array split by the delimiters as specified by each of the Command class
     */
    private String[] getArgsFromInput(Command.Type type, String input) {
        Command command = CommandList.get(type);
        if (command == null) {
            return null;
        }

        String inputArgsOnly = StringUtils.trim(input.substring(
                input.toLowerCase().indexOf(command.getName()) + command.getName().length()));

        Command.ArgInfo[] args = command.getArgs();
        String[] argsString = new String[args.length];
        int previousSplitIndex = 0;
        for (int i = 0; i < args.length; i++) {
            if (i == args.length - 1) {
                argsString[i] = StringUtils.trim(inputArgsOnly.substring(previousSplitIndex));
            } else {
                int nextSplitIndex = inputArgsOnly.indexOf(args[i].getIdentifier());
                argsString[i] = StringUtils.trim(inputArgsOnly.substring(previousSplitIndex, nextSplitIndex));
                previousSplitIndex = nextSplitIndex + args[i].getIdentifier().length();
            }
        }

        return argsString;
    }
}