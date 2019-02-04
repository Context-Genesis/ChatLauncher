package com.contextgenesis.chatlauncher.database.repository;

import com.contextgenesis.chatlauncher.database.dao.SuggestDao;
import com.contextgenesis.chatlauncher.database.entity.SuggestEntity;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

@Singleton
public class SuggestRepository {

    @Inject
    SuggestDao suggestDao;

    @Inject
    public SuggestRepository() {
    }

    // insert if possible else update
    public void upsert(SuggestEntity entity) {
        long id = suggestDao.insert(entity);
        if (id == -1) {
            suggestDao.updateClickCount(entity.getCommandName());
        }
    }

    public void printSuggestions() {
        List<SuggestEntity> suggestions = suggestDao.getAllSuggestions();
        Timber.i("Printing suggestions.......");
        for (SuggestEntity suggestEntity : suggestions) {
            Timber.i("Command:" + suggestEntity.getCommandName() + ", ArgType:" + suggestEntity.getArgType() + ", ClickCount:" + suggestEntity.getClickCount());
        }
    }
}
