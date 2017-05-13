package com.acterics.healthmonitor.ui.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.acterics.healthmonitor.R;

/**
 * Created by oleg on 13.05.17.
 */

public class AuthorizationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorization);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.holder_content, new SignInFragment())
                    .commit();
        }
    }
}
