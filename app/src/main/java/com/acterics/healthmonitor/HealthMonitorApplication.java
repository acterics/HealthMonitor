package com.acterics.healthmonitor;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by oleg on 12.05.17.
 */

public class HealthMonitorApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        Timber.e("onCreate");
    }
}
