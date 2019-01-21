package com.contextgenesis.chatlauncher;

import android.app.Application;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import timber.log.Timber;

public class RootApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        setupLogging();
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
                t.printStackTrace();
                Log.e(tag, message + " " + t.getMessage());
            }
        }
    }
}
