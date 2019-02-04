package com.contextgenesis.chatlauncher.manager.suggest;

import com.contextgenesis.chatlauncher.command.Command;
import com.contextgenesis.chatlauncher.command.CommandList;
import com.contextgenesis.chatlauncher.database.entity.SuggestEntity;
import com.contextgenesis.chatlauncher.database.repository.SuggestRepository;
import com.contextgenesis.chatlauncher.manager.app.AppManager;
import com.contextgenesis.chatlauncher.manager.call.ContactsManager;
import com.contextgenesis.chatlauncher.manager.input.InputMessage;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SuggestionManager {

    @Inject
    AppManager appManager;
    @Inject
    ContactsManager contactsManager;

    private final SuggestRepository suggestRepository;

    @Inject
    public SuggestionManager(SuggestRepository suggestRepository) {
        this.suggestRepository = suggestRepository;
    }

    public void improveSuggestions(String aliasCommand) {
        suggestRepository.upsert(new SuggestEntity(aliasCommand, Command.ArgInfo.Type.ALIAS.getId()));
    }

    /*
        improve suggestions for Commands, AppNames and Contacts
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
            } else if (contactsManager.isContactNameValid(args[i])) {
                suggestRepository.upsert(new SuggestEntity(args[i], Command.ArgInfo.Type.CONTACTS.getId()));
            }
        }
    }


    public void printAllSuggestions() {
        suggestRepository.printSuggestions();
    }


    // TODO: implement this for getting list of suggestions
    public List<SuggestEntity> requestSuggestions(String input) {

        return new ArrayList<>();
    }
}
