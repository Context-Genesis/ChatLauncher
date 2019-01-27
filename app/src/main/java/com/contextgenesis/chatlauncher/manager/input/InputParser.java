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

        // check if the input command is an alias
        if (aliasManager.containsAlias(input)) {
            Alias alias = aliasManager.getAlias(input);
            // changing to input to the input associated with the alias
            input = alias.getCommand().trim();
        }
        String trimInput = StringUtils.trim(input);

        Command.Type commandType = getCommandTypeFromInput(trimInput);

        // validate command enum
        if (commandType == Command.Type.UNKNOWN) {
            return InputMessage.invalidMessage(trimInput, commandType);
        }

        String[] args = getArgsFromInput(commandType, trimInput);

        // check on number of arguments
        if (args == null) {
            return InputMessage.invalidMessage(trimInput, commandType);
        } else if (commandType == Command.Type.ALIAS_ADD && args.length < 2) {
            return InputMessage.invalidMessage(trimInput, commandType);
        } else if (commandType == Command.Type.ALIAS_REMOVE && args.length < 1) {
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
                    case ALIAS_ADD:
                        if (i == 0) {
                            // arg[0] : aliasName
                            String aliasName = args[i];
                            // if the alias alreadyExists
                            if (aliasManager.containsAlias(aliasName.trim())) {
                                return InputMessage.invalidMessage(trimInput, commandType);
                            }
                        } else {
                            // arg[1] : command
                            // aliasCommand also needs to be validated
                            String aliasCommand = args[i].trim();
                            InputMessage aliasCommandValid = parse(aliasCommand);
                            if (!aliasCommandValid.isValid()) {
                                return InputMessage.invalidMessage(trimInput, commandType);
                            }
                        }
                        break;
                    case ALIAS_REMOVE:
                        // arg[0] : aliasName
                        String aliasName = args[i];
                        // if the alias doesnt exist
                        if (!aliasManager.containsAlias(aliasName.trim())) {
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
