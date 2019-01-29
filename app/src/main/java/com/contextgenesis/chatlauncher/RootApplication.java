package com.contextgenesis.chatlauncher;

import android.util.Log;

import com.contextgenesis.chatlauncher.dagger.AppComponent;
import com.contextgenesis.chatlauncher.dagger.DaggerAppComponent;
import com.contextgenesis.chatlauncher.manager.app.AppManager;
import com.contextgenesis.chatlauncher.manager.call.ContactsManager;
import com.contextgenesis.chatlauncher.rx.scheduler.BaseSchedulerProvider;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.NoEncryption;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import io.reactivex.Single;
import timber.log.Timber;

public class RootApplication extends DaggerApplication {

    @Inject
    BaseSchedulerProvider schedulerProvider;
    @Inject
    AppManager appManager;
    @Inject
    ContactsManager contactsManager;

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        Hawk.init(getApplicationContext())
                .setEncryption(new NoEncryption())
                .build();
        setupLogging();
        setupSlowCalls();
    }

    /**
     * todo ah we must get rid of this later on
     */
    private void setupSlowCalls() {
        Single.fromCallable(() -> {
            contactsManager.getContacts();
            appManager.getAppList();
            return true;
        })
                .observeOn(schedulerProvider.runOnBackground())
                .subscribeOn(schedulerProvider.runOnBackground());
    }

    public void setComponent(AppComponent appComponent) {
        this.appComponent = appComponent;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        appComponent = DaggerAppComponent.builder().application(this).build();
        return appComponent;
    }

    private void setupLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashlyticsTree());
        }
    }

    /**
     * todo update this was firebase for future builds
     */
    private class CrashlyticsTree extends Timber.Tree {
        @Override
        protected void log(int priority, @Nullable String tag, @NotNull String message, @Nullable Throwable t) {
            if (priority == Log.ERROR) {
                if (t != null) {
                    t.printStackTrace();
                }
                Log.e(tag, message);
            }
        }
    }
}
