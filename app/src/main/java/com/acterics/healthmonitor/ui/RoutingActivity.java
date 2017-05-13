package com.acterics.healthmonitor.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.acterics.healthmonitor.utils.NavigationUtils;
import com.acterics.healthmonitor.utils.PreferenceUtils;

/**
 * Created by oleg on 13.05.17.
 */

public class RoutingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!PreferenceUtils.isAuthorizad(getApplicationContext())) {
            NavigationUtils.toAuthorization(this);

        } else {
            NavigationUtils.toMain(this);

        }
        finish();

    }
}
