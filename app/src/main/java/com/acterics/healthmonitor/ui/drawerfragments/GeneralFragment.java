package com.acterics.healthmonitor.ui.drawerfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acterics.healthmonitor.R;

import butterknife.ButterKnife;

/**
 * Created by oleg on 12.05.17.
 */

public class GeneralFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_general, container, false);
        ButterKnife.bind(this, view);

        return view;
    }
}
