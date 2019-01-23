package com.contextgenesis.chatlauncher;

import android.app.Application;
import android.content.Context;

import com.contextgenesis.chatlauncher.command.CommandList;

/**
 * Until we don't have Dagger, I'm using this.
 * It's super ugly, but is a list of providers of all kinds of things used in the app
 * <p>
 * todo: make this Lifecycle aware
 */
public final class RootProviders {

    private static RootProviders rootProviders;
    private final Application application;
    private CommandList commandList;

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

    public CommandList getCommandList() {
        if (commandList == null) {
            commandList = new CommandList();
        }
        return commandList;
    }

    public Context getContext() {
        return application.getApplicationContext();
    }

}
