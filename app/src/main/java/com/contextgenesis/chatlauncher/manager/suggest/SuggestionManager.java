package com.contextgenesis.chatlauncher.manager.suggest;

import com.contextgenesis.chatlauncher.command.Command;
import com.contextgenesis.chatlauncher.command.CommandList;
import com.contextgenesis.chatlauncher.database.entity.SuggestEntity;
import com.contextgenesis.chatlauncher.database.repository.SuggestRepository;
import com.contextgenesis.chatlauncher.manager.app.AppInfo;
import com.contextgenesis.chatlauncher.manager.app.AppManager;
import com.contextgenesis.chatlauncher.manager.call.ContactInfo;
import com.contextgenesis.chatlauncher.manager.call.ContactsManager;
import com.contextgenesis.chatlauncher.manager.input.InputMessage;
import com.contextgenesis.chatlauncher.ui.SuggestionAdapter;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import timber.log.Timber;

@Singleton
public class SuggestionManager {

    private final AppManager appManager;
    private final ContactsManager contactsManager;
    private final SuggestRepository suggestRepository;

    @Inject
    SuggestionAdapter suggestionAdapter;

    @Inject
    public SuggestionManager(SuggestRepository suggestRepository, AppManager appManager, ContactsManager contactsManager) {
        this.suggestRepository = suggestRepository;
        this.appManager = appManager;
        this.contactsManager = contactsManager;
    }

    /*
          For first time populating the db, though this will increment each time the user opens the app
          initialize the db with SuggestionEntities for apps, contacts, commands and PredefinedInputs
          TODO: should be restricted to only once by storing isInitialized variable in map
    */
    public void initialize() {
        List<AppInfo> apps = appManager.getAppList();
        for (AppInfo app : apps) {
            suggestRepository.upsert(new SuggestEntity(app.getLabel(), Command.ArgInfo.Type.APPS.getId()));
        }

        // TODO: take contact permissions before itself else this would return no suggestions for contacts
        List<ContactInfo> contacts = contactsManager.getContacts();
        for (ContactInfo contact : contacts) {
            suggestRepository.upsert(new SuggestEntity(contact.getContactName(), Command.ArgInfo.Type.CONTACTS.getId()));
        }

        List<Command> commands = CommandList.COMMANDS;
        Set<String> distinctPredefinedInputs = new HashSet<>();
        for (Command command : commands) {
            suggestRepository.upsert(new SuggestEntity(command.getName(), Command.ArgInfo.Type.COMMAND.getId()));
            Command.ArgInfo[] args = command.getArgs();
            for (Command.ArgInfo argInfo : args) {
                String[] predefinedInputs = argInfo.getPredefinedInputs();
                distinctPredefinedInputs.addAll(Arrays.asList(predefinedInputs));
            }
        }
        for (String predefinedInput : distinctPredefinedInputs) {
            suggestRepository.upsert(new SuggestEntity(predefinedInput, Command.ArgInfo.Type.PREDEFINED.getId()));
        }
    }

    /*
        improve suggestions for Alias
     */
    public void improveSuggestions(String aliasCommand) {
        suggestRepository.upsert(new SuggestEntity(aliasCommand, Command.ArgInfo.Type.ALIAS.getId()));
    }

    /*
        improve suggestions for Commands, AppNames, Contacts and
        not improving suggestions for Predefined inputs as it makes sense to show inputs like "on" and "off" in the same order each time
     */
    public void improveSuggestions(InputMessage inputMessage) {
        String inputCommand = CommandList.get(inputMessage.getCommandType()).getName();
        suggestRepository.upsert(new SuggestEntity(inputCommand, Command.ArgInfo.Type.COMMAND.getId()));

        String args[] = inputMessage.getArgs();
        for (int i = 0; i < args.length; i++) {
            if (StringUtils.isEmpty(args[i])) {
                continue;
            }
            if (appManager.isAppNameValid(args[i])) {
                suggestRepository.upsert(new SuggestEntity(args[i], Command.ArgInfo.Type.APPS.getId()));
            } else if (contactsManager.isContactsPermissionsGranted() && contactsManager.isContactNameValid(args[i])) {
                suggestRepository.upsert(new SuggestEntity(args[i], Command.ArgInfo.Type.CONTACTS.getId()));
            }
        }

        // set p=launch perplexy, include "p" in suggestions for alias
        if (inputMessage.getCommandType().equals(Command.Type.ALIAS_ADD)) {
            improveSuggestions(args[0]);
        }

        // remove alias from suggestion table as well
        if (inputMessage.getCommandType().getId() == Command.Type.ALIAS_REMOVE.getId()) {
            removeSuggestion(args[0]);
        }

        // this is needed to reflect suggestions immediately
        startSuggestionThread("");

    }

    // TODO: Capture events: app uninstall{should also delete all alias for appName},
    // TODO: contact delete
    //remove an entry from suggestion table
    public void removeSuggestion(String suggestion) {
        suggestRepository.removeSuggestion(suggestion);
    }

    public void printAllSuggestions() {
        suggestRepository.printSuggestions();
    }

    /*
        Step1: check whether a command exists or not
        Step2: if command not present return the list of probable commands
        Step3: else check the commands argument type and suggest accordingly
    */
    public Single<List<SuggestEntity>> requestSuggestions(String input) {
        List<Command> commands = CommandList.COMMANDS;
        Command inputCommand = null;
        for (Command command : commands) {
            if (input.contains(command.getName())) {
                inputCommand = command;
            }
        }

        List<Integer> argTypes = new ArrayList<>();
        if (inputCommand == null) {
            argTypes.add(Command.ArgInfo.Type.COMMAND.getId());
            argTypes.add(Command.ArgInfo.Type.ALIAS.getId());
            // TODO: later when we get rid of launch, add APP here
            return suggestRepository.getSuggestions(input, argTypes);

        } else {
            Command.ArgInfo[] argsInfo = inputCommand.getArgs();

            String inputArgsOnly = StringUtils.stripStart(input.substring(
                    input.toLowerCase().indexOf(inputCommand.getName()) + inputCommand.getName().length()), " ");

            Timber.i(input.toLowerCase().indexOf(inputCommand.getName()) + inputCommand.getName().length() + "");

            for (int i = 0; i < argsInfo.length; i++) {
                if (inputArgsOnly.contains(argsInfo[i].getIdentifier())) {
                    // remove the previous arguments from the search
                    inputArgsOnly = inputArgsOnly.substring(inputArgsOnly.toLowerCase().indexOf(argsInfo[i].getIdentifier()) + argsInfo[i].getIdentifier().length());
                    if ((i + 1) != argsInfo.length && inputArgsOnly.contains(argsInfo[i + 1].getIdentifier())) {
                        continue;
                    }
                    if (argsInfo[i].hasPredefinedInputs()) {
                        return suggestRepository.getPredefinedInputSuggestions(inputArgsOnly, argsInfo[i].getPredefinedInputs(), Command.ArgInfo.Type.PREDEFINED.getId());
                    } else {
                        // special case: eg set wa = launch , we need suggestions for appNames
                        if (inputCommand.getType().equals(Command.Type.ALIAS_ADD) && argsInfo[i].getIdentifier().equals("=")) {
                            return requestSuggestions(inputArgsOnly);
                        }
                        argTypes.add(argsInfo[i].getType().getId());
                        return suggestRepository.getSuggestions(inputArgsOnly, argTypes);
                    }
                } else {
                    // if the identifier for a first mandatory argument is missing, return nothing
                    if (argsInfo[i].isRequired()) {
                        break;
                    }
                }
            }
        }

        // no suggestions
        return null;
    }

    // TODO: Capture events: app install
    // should insert in db

    // TODO: contact add/edit{should also get changed in alias say "call Dhruv"}
    // updateSuggestionTable()


    public void startSuggestionThread(String input) {
        requestSuggestions(input.toLowerCase()).subscribe(suggestEntities -> {
            Timber.i("Suggestions");
            List<SuggestEntity> suggestions = suggestionAdapter.getSuggestions();
            suggestions.removeAll(suggestions);
            for (SuggestEntity suggestion : suggestEntities) {
                Timber.i(suggestion.getCommandName() + " , " + suggestion.getClickCount());
                suggestions.add(suggestion);
            }
            suggestionAdapter.notifyDataSetChanged();
        });
    }

}
