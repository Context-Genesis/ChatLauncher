package com.contextgenesis.chatlauncher.database.repository;

import com.contextgenesis.chatlauncher.database.dao.SuggestDao;
import com.contextgenesis.chatlauncher.database.entity.SuggestEntity;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
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

    public Single<List<SuggestEntity>> getSuggestions(String input, List<Integer> argTypes) {
        return Single.fromCallable(() -> suggestDao.getSuggestions(input, argTypes)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Single<List<SuggestEntity>> getPredefinedInputSuggestions(String input, String[] predefinedInput, int argType) {
        return Single.fromCallable(() -> suggestDao.getPredefinedInputs(input, predefinedInput, argType)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
