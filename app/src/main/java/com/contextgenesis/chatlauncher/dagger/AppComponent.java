package com.contextgenesis.chatlauncher.dagger;

import android.app.Application;

import com.contextgenesis.chatlauncher.RootApplication;
import com.contextgenesis.chatlauncher.ui.ShortcutsCardView;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;


/**
 * This is a Dagger component. Refer to RootApplication for the list
 * of Dagger components used in this application.
 * <p>
 * Even though Dagger allows annotating a {@link Component} as a singleton, the code
 * itself must ensure only one instance of the class is created. This is done in RootApplication.
 * // {@link AndroidSupportInjectionModule}
 * // is the module from Dagger.Android that helps with the generation
 * // and location of subcomponents.
 */

@Singleton
@Component(modules = {
        AppModule.class,
        AndroidComponentBindingModule.class,
        AndroidSupportInjectionModule.class})

public interface AppComponent extends AndroidInjector<RootApplication> {

    void inject(ShortcutsCardView shortcutsCardView);

    // Gives us syntactic sugar. we can then do DaggerAppComponent.builder().application(this).build().inject(this);
    // never having to instantiate any modules or say which module we are passing the application to.
    // Application will just be provided into our app graph now.
    @Component.Builder
    interface Builder {

        @BindsInstance
        AppComponent.Builder application(Application application);

        AppComponent build();
    }
}
