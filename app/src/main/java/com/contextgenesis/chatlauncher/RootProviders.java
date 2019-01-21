package com.contextgenesis.chatlauncher;

import android.app.Application;
import android.content.Context;

import com.contextgenesis.chatlauncher.manager.app.AppManager;

/**
 * Until we don't have Dagger, I'm using this.
 * It's super ugly, but is a list of providers of all kinds of things used in the app
 * <p>
 * todo: make this Lifecycle aware
 */
public final class RootProviders {

    private static RootProviders rootProviders;
    private final Application application;
    private AppManager appManager;

    private RootProviders(Application application) {
        this.application = application;
    }

    public static void init(Application application) {
        if (rootProviders == null) {
            rootProviders = new RootProviders(application);
        }
    }

    public static RootProviders get() {
        return rootProviders;
    }

    public AppManager getAppManager() {
        if (appManager == null) {
            appManager = new AppManager(application.getApplicationContext().getPackageManager());
        }
        return appManager;
    }

    public Context getContext() {
        return application.getApplicationContext();
    }

}
