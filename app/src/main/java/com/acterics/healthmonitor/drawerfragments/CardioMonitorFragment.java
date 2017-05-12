package com.acterics.healthmonitor.drawerfragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acterics.healthmonitor.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by oleg on 12.05.17.
 */

public class CardioMonitorFragment extends Fragment {

    public static final String ACTION_DATA = "com.acterics.healthmonitor.drawerfragments.ACTION_DATA";
    public static final String ACTION_LOST_CONNECTION = "com.acterics.healthmonitor.drawerfragments.ACTION_LOST_CONNECTION";
    public static final String KEY_DATA = "com.acterics.healthmonitor.drawerfragments.KEY_DATA";

    @BindView(R.id.loadingPanel) View loadingPanel;

    private BroadcastReceiver cardioDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cardio_monitor, container, false);
        ButterKnife.bind(this, view);

        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        IntentFilter cardioDataIntentFilter = new IntentFilter();
        cardioDataIntentFilter.addAction(ACTION_DATA);
        cardioDataIntentFilter.addAction(ACTION_LOST_CONNECTION);
        getActivity().registerReceiver(cardioDataReceiver, cardioDataIntentFilter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().unregisterReceiver(cardioDataReceiver);
    }
}
