package com.contextgenesis.chatlauncher.database.repository;

import com.contextgenesis.chatlauncher.database.dao.AliasDao;
import com.contextgenesis.chatlauncher.database.entity.AliasEntity;
import com.contextgenesis.chatlauncher.events.OutputMessageEvent;
import com.contextgenesis.chatlauncher.rx.RxBus;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Singleton
public class AliasRepository {

    @Inject
    RxBus rxBus;
    @Inject
    AliasDao aliasDao;

    @Inject
    public AliasRepository() {
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

    /**
     * todo remove the postOutput call from here; this shouldn't be the job of the Repository; it is
     * that of the callee of this function, to post it if it so desires.
     */
    void postOutput(String output) {
        rxBus.post(new OutputMessageEvent(output));
    }
}