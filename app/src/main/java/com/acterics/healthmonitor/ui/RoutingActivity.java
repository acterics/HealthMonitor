package com.acterics.healthmonitor.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.acterics.healthmonitor.utils.NavigationUtils;
import com.acterics.healthmonitor.utils.PreferenceUtils;

/**
 * Created by oleg on 13.05.17.
 * Launch activity. Has no UI.
 * Define application state and navigate to actual activity.
 */

public class RoutingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!PreferenceUtils.isAuthorized(getApplicationContext())) {
            NavigationUtils.toAuthorization(this, false);

        } else {
            NavigationUtils.toMain(this);

        }
        finish();

    }
}
