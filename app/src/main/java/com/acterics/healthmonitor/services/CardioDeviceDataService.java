package com.acterics.healthmonitor.services;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.acterics.healthmonitor.R;
import com.acterics.healthmonitor.ui.drawerfragments.CardioMonitorFragment;

import timber.log.Timber;

/**
 * Created by oleg on 12.05.17.
 */

public class CardioDeviceDataService extends Service {
    public static final String ACTION_START_CONNECTION = "com.acterics.healthmonitor.ACTION_START_CONNECTION";
    public static final String ACTION_START_COMMUNICATE = "com.acterics.healthmonitor.ACTION_START_COMMUNICATE";
    public static final String ACTION_DEVICE_CONNECTION_FAIL = "com.acterics.healthmonitor.ACTION_DEVICE_CONNECTION_FAIL";




    private BluetoothDevice device = null;
    private BluetoothAdapter bluetoothAdapter;

    private BroadcastReceiver discoverDeviceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice found = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Timber.e("ConnectDeviceIntentService onReceive: ACTION_FOUND %s", found.getName());
                if (found.getName().equals(getString(R.string.bluetooth_device_name))) {
                    device = found;
                    bluetoothAdapter.cancelDiscovery();
                    unregisterDeviceDiscoveryBroadcast();
                    onStartCommunicate();
                }
            } else if (intent.getAction().equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                Timber.e("ConnectDeviceIntentService onReceive: ACTION_DISCOVERY_FINISHED");
                onStartCommunicate();
                unregisterDeviceDiscoveryBroadcast();

            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(ACTION_START_CONNECTION)) {
            Timber.e("onStartCommand: start connection");
            registerDeviceDiscoveryBroadcast();
        }
        if (intent.getAction().equals(ACTION_DEVICE_CONNECTION_FAIL)) {
            Timber.e("onStartCommand: connection fail");
        }
        if (intent.getAction().equals(ACTION_START_COMMUNICATE)) {
            Timber.e("onStartCommand: start communicate");
            onStartCommunicate();
        }

        return START_STICKY;
    }


    private void onStartCommunicate() {
//        startService(new Intent(this, MockDataIntentService.class));
        Timber.i("onStartCommunicate");
        if (bluetoothAdapter == null) {
            Timber.i("onStartCommunicate adapter null");
            sendBroadcast(new Intent(CardioMonitorFragment.ACTION_UNAVAILABLE));
        } else if (!bluetoothAdapter.isEnabled()) {
            Timber.i("onStartCommunicate bluetooth disabled");
            sendBroadcast(new Intent(CardioMonitorFragment.ACTION_ENABLE_BLUETOOTH));
        } else if (device == null) {
            Timber.i("onStartCommunicate device null");
            sendBroadcast(new Intent(CardioMonitorFragment.ACTION_LOST_CONNECTION));
        } else {
            Timber.e("onStartCommunicate: device connected");
            onDeviceConnected();
        }
    }

    private void onDeviceConnected() {
        Intent intent = new Intent(this, ClientSocketIntentService.class);
        intent.putExtra(ClientSocketIntentService.EXTRA_DEVICE, device);
        startService(intent);
    }

    private void registerDeviceDiscoveryBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(discoverDeviceReceiver, intentFilter);
        bluetoothAdapter.startDiscovery();
    }

    private void unregisterDeviceDiscoveryBroadcast() {
        unregisterReceiver(discoverDeviceReceiver);
    }

}
