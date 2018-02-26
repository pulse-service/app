package com.electrocraft.nirzo.pluse.controller;

import android.app.Application;

import timber.log.Timber;

/**
 * <p>
 * The Application class in Android is the base class within an Android app that contains all
 * other components such as activities and services.
 * </p>
 *
 * @author Faisal
 * @since 2/26/2018.
 */

public class ApplicationClass extends com.activeandroid.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());
    }
}
