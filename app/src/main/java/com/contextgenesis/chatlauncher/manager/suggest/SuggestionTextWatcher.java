package com.contextgenesis.chatlauncher.manager.suggest;

import android.text.Editable;
import android.text.TextWatcher;

import com.contextgenesis.chatlauncher.database.entity.SuggestEntity;
import com.contextgenesis.chatlauncher.rx.scheduler.BaseSchedulerProvider;
import com.contextgenesis.chatlauncher.ui.SuggestionAdapter;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.Single;
import timber.log.Timber;

@Singleton
public class SuggestionTextWatcher implements TextWatcher {

    @Inject
    SuggestionManager suggestionManager;
    @Inject
    BaseSchedulerProvider schedulerProvider;

    @Inject
    public SuggestionTextWatcher() {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String input = s.toString();
        suggestionManager.startSuggestionThread(input);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
