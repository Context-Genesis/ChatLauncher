package com.contextgenesis.chatlauncher.dagger;

import com.contextgenesis.chatlauncher.dagger.scopes.ActivityScoped;
import com.contextgenesis.chatlauncher.ui.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * We want Dagger.Android to create a Subcomponent which has a parent Component of whichever module AndroidComponentBindingModule is on,
 * in our case that will be AppComponent. The beautiful part about this setup is that you never need to tell AppComponent that it is going to have all these subcomponents
 * nor do you need to tell these subcomponents that AppComponent exists.
 * We are also telling Dagger.Android that this generated SubComponent needs to include the specified modules and be aware of a scope annotation @ActivityScoped
 * When Dagger.Android annotation processor runs it will create 4 subcomponents for us.
 */

@Module
@SuppressWarnings("PMD.CouplingBetweenObjects")
public abstract class AndroidComponentBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector
    abstract MainActivity mainActivity();

}