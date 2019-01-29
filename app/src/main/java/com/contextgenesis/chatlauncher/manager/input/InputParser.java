package com.contextgenesis.chatlauncher.manager.input;

import com.contextgenesis.chatlauncher.command.Alias;
import com.contextgenesis.chatlauncher.command.Command;
import com.contextgenesis.chatlauncher.command.CommandList;
import com.contextgenesis.chatlauncher.manager.alias.AliasManager;
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
    AliasManager aliasManager;

    @Inject
    public InputParser() {
    }

    /**
     * This method is responsible for parsing the entire message along with its arguments,
     * and flags whether it is valid or not.
     */
    public InputMessage parse(String input) {
        String trimInput = StringUtils.trim(input);

        // check if the input command is an alias
        if (aliasManager.containsAlias(trimInput)) {
            Alias alias = aliasManager.getAlias(trimInput);
            // changing to input to the input associated with the alias
            trimInput = alias.getCommand().trim();
        }

        Command.Type commandType = getCommandTypeFromInput(trimInput);

        // validate command enum
        if (commandType == Command.Type.UNKNOWN) {
            return InputMessage.invalidMessage(trimInput, commandType);
        }

        String[] args = getArgsFromInput(commandType, trimInput);

        // check on number of arguments
        if (args == null) {
            return InputMessage.invalidMessage(trimInput, commandType);
        } else if (commandType == Command.Type.ALIAS_ADD && args.length != 2) {
            return InputMessage.invalidMessage(trimInput, commandType);
        } else if (commandType == Command.Type.ALIAS_REMOVE && args.length != 1) {
            return InputMessage.invalidMessage(trimInput, commandType);
        }

        // check for the data within the args
        Command.ArgInfo[] argsInfo = CommandList.get(commandType).getArgs();
        for (int i = 0; i < argsInfo.length; i++) {
            if (argsInfo[i].isRequired()) {
                switch (argsInfo[i].getType()) {
                    case APPS:
                        if (!appManager.isAppNameValid(args[i])) {
                            return InputMessage.invalidMessage(trimInput, commandType);
                        }
                        break;
                    case CONTACTS:
                        if (!contactsManager.isContactNameValid(args[i]) || StringUtils.isNumeric(args[i])) {
                            return InputMessage.invalidMessage(trimInput, commandType);
                        }
                        break;
                    case ALIAS:
                        String aliasName = args[i];
                        // if the alias doesnt exist
                        if (!aliasManager.containsAlias(aliasName.trim())) {
                            return InputMessage.invalidMessage(trimInput, commandType);
                        }
                        break;
                    case COMMAND:
                        // command needs to be validated
                        String command = args[i].trim();
                        InputMessage commandInputMessage = parse(command);
                        if (!commandInputMessage.isValid()) {
                            return InputMessage.invalidMessage(trimInput, commandType);
                        }
                        break;
                    case PREDEFINED:
                        boolean oneValidFound = false;
                        for (String validInput : argsInfo[i].getPredefinedInputs()) {
                            if (validInput.equalsIgnoreCase(args[0])) {
                                oneValidFound = true;
                            }
                        }
                        if (!oneValidFound) {
                            return InputMessage.invalidMessage(trimInput, commandType);
                        }
                        break;
                    case NO_SUGGEST:
                        // for commandType alias_add, aliasName should always be unique
                        if (commandType == Command.Type.ALIAS_ADD && i == 0) {
                            aliasName = args[i];
                            if (aliasManager.containsAlias(aliasName.trim())) {
                                return InputMessage.invalidMessage(trimInput, commandType);
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }

        return InputMessage.validMessage(trimInput, commandType, args);
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
        String previousNotEmpty = "";
        int previousNotEmptyIndex = 0;
        for (int i = 0; i < args.length; i++) {
            if (i == 0) {
                argsString[i] = inputArgsOnly;
                previousNotEmpty = argsString[0];
                continue;
            }
            String identifier = args[i].getIdentifier();
            if (!StringUtils.isEmpty(identifier) && previousNotEmpty.contains(identifier)) {
                String afterSplit[] = previousNotEmpty.split(identifier, 2);
                argsString[i] = afterSplit[1];
                argsString[previousNotEmptyIndex] = afterSplit[0];

                // setting the previous parameters correctly
                previousNotEmpty = argsString[i];
                previousNotEmptyIndex = i;
            } else if (args[i].isRequired()) {
                // error scenario when we dont have a mandatory argument
                return null;
            }
        }

        return argsString;
    }

}
