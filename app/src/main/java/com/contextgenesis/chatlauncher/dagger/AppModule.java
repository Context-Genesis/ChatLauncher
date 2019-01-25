package com.contextgenesis.chatlauncher.dagger;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;

import com.contextgenesis.chatlauncher.rx.RxBus;
import com.contextgenesis.chatlauncher.rx.RxEventBus;
import com.contextgenesis.chatlauncher.rx.scheduler.BaseSchedulerProvider;
import com.contextgenesis.chatlauncher.rx.scheduler.SchedulerProvider;

import javax.inject.Singleton;

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

    @Provides
    PackageManager providePackageManager(Context context) {
        return context.getPackageManager();
    }

    @Provides
    @Singleton
    RxBus provideRxEventBus() {
        return new RxEventBus();
    }

    @Provides
    @Singleton
    BaseSchedulerProvider provideSchedulerProvider() {
        return new SchedulerProvider();
    }
}
