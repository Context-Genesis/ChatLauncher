package com.contextgenesis.chatlauncher;

import android.content.Context;

import com.contextgenesis.chatlauncher.manager.app.AppManager;

/**
 * Until we don't have Dagger, I'm using this.
 * It's super ugly, but is a list of providers of all kinds of things used in the app
 * <p>
 * todo: make this Lifecycle aware
 */
public class RootProviders {

    private static RootProviders rootProviders;

    private AppManager appManager;
    private Context context;

    private RootProviders(Context context) {
        this.context = context;
    }

    public static void init(Context context) {
        if (rootProviders == null) {
            rootProviders = new RootProviders(context);
        }
    }

    public static RootProviders get() {
        return rootProviders;
    }

    public AppManager getAppManager() {
        if (appManager == null) {
            appManager = new AppManager(context.getPackageManager());
        }
        return appManager;
    }

    public Context getContext() {
        return context;
    }

}
