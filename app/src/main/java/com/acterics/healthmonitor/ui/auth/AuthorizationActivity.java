package com.acterics.healthmonitor.ui.auth;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.acterics.healthmonitor.R;
import com.acterics.healthmonitor.utils.PreferenceUtils;

import timber.log.Timber;

/**
 * Created by oleg on 13.05.17.
 */

public class AuthorizationActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    public static final String AUTH_FLOW = "com.acterics.healthmonitor.ui.auth.AUTH_FLOW";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!checkPermission()) {
                return;
            }
        }
        if (savedInstanceState == null) {
            Timber.e("onCreate: savedInstanceState = null");
            Fragment fragment;
            if (PreferenceUtils.isLastUserExists(getApplicationContext())) {
                fragment = new WelcomeFragment();
            } else {
                fragment = new StartFragment();
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .add(R.id.holder_content, fragment)
                    .commit();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                // You don't have permission
                checkPermission();

            }
            else
            {
                //do as per your logic
            }

        }
    }

    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, PERMISSION_REQUEST_CODE);
                return false;
            }
        }
        return true;
    }
}
