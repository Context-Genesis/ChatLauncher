package com.contextgenesis.chatlauncher.dagger;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;

import dagger.Module;
import dagger.Provides;

/**
 * Provides common dependencies
 */

@Module
@SuppressWarnings("PMD.CouplingBetweenObjects")
public class AppModule {

    //expose Application as an injectable context
    @Provides
    Context bindContext(Application application) {
        return application;
    }

    /*@Singleton
    @Provides
    EasyLocationMod provideLocation(Context context, RxBus rxBus) {
        return new EasyLocationMod(context, rxBus);
    }*/

    @Provides
    PackageManager providePackageManager(Context context) {
        return context.getPackageManager();
    }
}
