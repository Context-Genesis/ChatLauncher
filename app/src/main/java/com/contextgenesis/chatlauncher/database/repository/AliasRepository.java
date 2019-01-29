package com.contextgenesis.chatlauncher.database.repository;

import android.content.Context;
import android.os.AsyncTask;

import com.contextgenesis.chatlauncher.database.AliasDatabase;
import com.contextgenesis.chatlauncher.database.dao.AliasDao;
import com.contextgenesis.chatlauncher.database.entity.AliasEntity;
import com.contextgenesis.chatlauncher.events.OutputMessageEvent;
import com.contextgenesis.chatlauncher.rx.RxBus;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.room.Room;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class AliasRepository {

    // alias database object
    private AliasDatabase aliasDatabase;

    private AliasDao aliasDao;

    @Inject
    RxBus rxBus;

    @Inject
    public AliasRepository(Context context) {
        aliasDatabase = Room.databaseBuilder(context, AliasDatabase.class, "aliasDatabase").build();
        aliasDao = aliasDatabase.aliasDao();
    }

    public void insert(String aliasName, String command) {
        AliasEntity aliasEntity = new AliasEntity(aliasName, command);
        aliasDao.insert(aliasEntity);
        postOutput("Alias insert successful");
    }

    public void delete(String aliasName) {
        aliasDao.deleteByAliasName(aliasName);
        postOutput("Alias delete successful");
    }

    public Single<List<AliasEntity>> getAllAlias() {
        return Single.fromCallable(() -> aliasDao.getAllAlias()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    void postOutput(String output) {
        rxBus.post(new OutputMessageEvent(output));
    }
}