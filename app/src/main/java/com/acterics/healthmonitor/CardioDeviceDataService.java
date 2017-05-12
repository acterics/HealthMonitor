package com.acterics.healthmonitor;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.acterics.healthmonitor.drawerfragments.CardioMonitorFragment;

import timber.log.Timber;

/**
 * Created by oleg on 12.05.17.
 */

public class CardioDeviceDataService extends Service {
    public static final String ACTION_START_CONNECTION = "com.acterics.healthmonitor.ACTION_START_CONNECTION";
    public static final String ACTION_DEVICE_CONNECTED = "com.acterics.healthmonitor.ACTION_DEVICE_CONNECTED";
    public static final String ACTION_START_COMMUNICATE = "com.acterics.healthmonitor.ACTION_START_COMMUNICATE";
    public static final String ACTION_DEVICE_CONNECTION_FAIL = "com.acterics.healthmonitor.ACTION_DEVICE_CONNECTION_FAIL";

    public static final String KEY_MESSAGE = "com.acterics.healthmonitor.KEY_MESSAGE";
    public static final String KEY_DEVICE = "com.acterics.healthmonitor.KEY_DEVICE";


    private BluetoothDevice device = null;
    private BluetoothAdapter bluetoothAdapter;

    private BroadcastReceiver discoverDeviceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BluetoothDevice.ACTION_FOUND)) {
                Timber.e("ConnectDeviceIntentService onReceive: ACTION_FOUND");
                BluetoothDevice found = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null && device.getName().equals(getString(R.string.bluetooth_device_name))) {
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
        if (intent.getAction().equals(ACTION_DEVICE_CONNECTED)) {
            device = intent.getParcelableExtra(KEY_DEVICE);
            Timber.e("onStartCommand: connection success");
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
        if (bluetoothAdapter == null) {
            sendBroadcast(new Intent(CardioMonitorFragment.ACTION_UNAVAILABLE));
        } else if (!bluetoothAdapter.isEnabled()) {
            sendBroadcast(new Intent(CardioMonitorFragment.ACTION_ENABLE_BLUETOOTH));
        } else if (device == null) {
            sendBroadcast(new Intent(CardioMonitorFragment.ACTION_LOST_CONNECTION));
        } else {
            Timber.e("onStartCommunicate: device connected");
        }
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
