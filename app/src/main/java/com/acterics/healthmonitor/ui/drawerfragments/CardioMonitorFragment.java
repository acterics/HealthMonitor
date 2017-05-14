package com.acterics.healthmonitor.ui.drawerfragments;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acterics.healthmonitor.data.models.CardioPlotModel;
import com.acterics.healthmonitor.ui.plot.CardioPlotView;
import com.acterics.healthmonitor.services.CardioDeviceDataService;
import com.acterics.healthmonitor.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;

/**
 * Created by oleg on 12.05.17.
 */

public class CardioMonitorFragment extends Fragment {

    public static final String ACTION_DATA = "com.acterics.healthmonitor.ui.drawerfragments.ACTION_DATA";
    public static final String ACTION_LOST_CONNECTION = "com.acterics.healthmonitor.ui.drawerfragments.ACTION_LOST_CONNECTION";
    public static final String ACTION_ENABLE_BLUETOOTH = "com.acterics.healthmonitor.ui.drawerfragments.ACTION_ENABLE_BLUETOOTH";
    public static final String ACTION_UNAVAILABLE = "com.acterics.healthmonitor.ui.drawerfragments.ACTION_UNAVAILABLE";
    public static final String EXTRA_DEVICE_DATA = "com.acterics.healthmonitor.ui.drawerfragments.EXTRA_DEVICE_DATA";

    private static final int REQUEST_ENABLE_BLUETOOTH = 1;

    @BindView(R.id.loadingPanel) View loadingPanel;
    @BindView(R.id.cardio_plot_view) CardioPlotView cardioPlotView;

    AlertDialog.Builder builder;

    private BroadcastReceiver cardioDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Timber.i("onReceive");
            loadingPanel.setVisibility(View.GONE);
            switch (intent.getAction()) {
                case ACTION_ENABLE_BLUETOOTH:
                    turnOnBluetooth();
                    break;
                case ACTION_LOST_CONNECTION:
                    connectDevice();
                    break;
                case ACTION_UNAVAILABLE:
                    unavailable();
                    break;
                case ACTION_DATA:
                    cardioPlotView.addValue(new CardioPlotModel(intent.getIntExtra(EXTRA_DEVICE_DATA, 0)));
                    break;
            }
            loadingPanel.setVisibility(View.GONE);
        }
    };

    private Dialog.OnClickListener onEnableBluetooth = (dialog, which) -> {
        Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH);
    };

    private Dialog.OnClickListener connectDevice = (dialog, which) -> {
        Timber.e("connectDevice");
        loadingPanel.setVisibility(View.VISIBLE);
        Intent startConnection = new Intent(getContext(), CardioDeviceDataService.class);
        startConnection.setAction(CardioDeviceDataService.ACTION_START_CONNECTION);
        getActivity().startService(startConnection);
    };



    private void turnOnBluetooth() {
        builder.setTitle(R.string.warning)
                .setMessage(R.string.message_enable_bluetooth)
                .setPositiveButton(R.string.enable, onEnableBluetooth)
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel())
                .setOnCancelListener(dialog -> onCancelDialog())
                .create()
                .show();

    }

    private void connectDevice() {
        builder.setTitle(R.string.warning)
                .setMessage(R.string.message_connect_device)
                .setPositiveButton(R.string.connect, connectDevice)
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel())
                .setOnCancelListener(dialog -> onCancelDialog())
                .create()
                .show();

    }

    private void onCancelDialog() {
        getFragmentManager().beginTransaction()
                .replace(R.id.holder_content, new GeneralFragment())
                .commit();
    }

    private void unavailable() {
        builder.setTitle(R.string.error)
                .setMessage(R.string.error_cardio_monitoring_unavailable)
                .setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss())
                .setOnDismissListener(dialog -> onCancelDialog())
                .create()
                .show();
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cardio_monitor, container, false);
        ButterKnife.bind(this, view);
        builder = new AlertDialog.Builder(getContext(), R.style.DefaultDialog);
        loadingPanel.setVisibility(View.VISIBLE);
        Intent startCommunicationIntent = new Intent(getContext(), CardioDeviceDataService.class);
        startCommunicationIntent.setAction(CardioDeviceDataService.ACTION_START_COMMUNICATE);
        getActivity().startService(startCommunicationIntent);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        IntentFilter cardioDataIntentFilter = new IntentFilter();
        cardioDataIntentFilter.addAction(ACTION_DATA);
        cardioDataIntentFilter.addAction(ACTION_LOST_CONNECTION);
        cardioDataIntentFilter.addAction(ACTION_ENABLE_BLUETOOTH);
        cardioDataIntentFilter.addAction(ACTION_UNAVAILABLE);
        getActivity().registerReceiver(cardioDataReceiver, cardioDataIntentFilter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().unregisterReceiver(cardioDataReceiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        loadingPanel.setVisibility(View.GONE);
        if (requestCode == REQUEST_ENABLE_BLUETOOTH && resultCode == RESULT_OK) {
            Intent startCommunicationIntent = new Intent(getContext(), CardioDeviceDataService.class);
            startCommunicationIntent.setAction(CardioDeviceDataService.ACTION_START_COMMUNICATE);
            getActivity().startService(startCommunicationIntent);
        } else {
            Timber.e("onActivityResult: something wrong");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
