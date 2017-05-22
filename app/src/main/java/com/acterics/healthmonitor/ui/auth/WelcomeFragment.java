package com.acterics.healthmonitor.ui.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.acterics.healthmonitor.R;
import com.acterics.healthmonitor.utils.PreferenceUtils;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by oleg on 20.05.17.
 */

public class WelcomeFragment extends Fragment {

    @BindView(R.id.tv_title) TextView tvTitle;
    @BindView(R.id.im_last_user_avatar) ImageView imAvatar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome_back, container, false);
        ButterKnife.bind(this, view);


        Glide.with(getContext())
                .load(PreferenceUtils.getLastUserAvatar(getContext()))
                .centerCrop()
                .into(imAvatar);
        tvTitle.setText(getString(R.string.auth_welcome, PreferenceUtils.getLastUserName(getContext())));

        getFragmentManager()
                .beginTransaction()
                .add(R.id.holder_buttons, new AuthButtonsFragment())
                .commit();





        return view;
    }
}
