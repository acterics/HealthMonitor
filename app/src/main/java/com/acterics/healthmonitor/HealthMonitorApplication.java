package com.acterics.healthmonitor;

import android.app.Application;

import com.acterics.healthmonitor.utils.NavigationUtils;
import com.acterics.healthmonitor.utils.PreferenceUtils;
import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

/**
 * Created by oleg on 12.05.17.
 */

public class HealthMonitorApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();



        if (PreferenceUtils.isAuthorized(this)) {
            NavigationUtils.startListenCardion(this);
        }

        Timber.plant(new Timber.DebugTree());
        if (BuildConfig.DEBUG) {
            Stetho.newInitializerBuilder(this)
                    .enableDumpapp(
                            Stetho.defaultDumperPluginsProvider(this))
                    .enableWebKitInspector(
                            Stetho.defaultInspectorModulesProvider(this))
                    .build();
            Stetho.initializeWithDefaults(this);
        } else {
            Fabric.with(this, new Crashlytics());
        }
    }
}
