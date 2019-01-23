package com.contextgenesis.chatlauncher;

import android.util.Log;

import com.contextgenesis.chatlauncher.dagger.AppComponent;
import com.contextgenesis.chatlauncher.dagger.DaggerAppComponent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import timber.log.Timber;

public class RootApplication extends DaggerApplication {

    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        setupLogging();
        RootProviders.init(this);
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
