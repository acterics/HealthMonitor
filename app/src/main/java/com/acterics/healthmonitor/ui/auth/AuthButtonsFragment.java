package com.acterics.healthmonitor.ui.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acterics.healthmonitor.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by oleg on 20.05.17.
 */

public class AuthButtonsFragment extends Fragment {



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auth_buttons, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.holder_email)
    void onEmailClick() {
        getFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack(AuthorizationActivity.AUTH_FLOW)
                .replace(R.id.holder_content, new SignInFragment())
                .commit();
    }
}
