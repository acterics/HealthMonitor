package com.acterics.healthmonitor;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.acterics.healthmonitor.drawerfragments.CardioMonitorFragment;

/**
 * Created by oleg on 12.05.17.
 */

public class CardioDeviceDataService extends Service {
    public static final String ACTION_START_CONNECTION = "com.acterics.healthmonitor.ACTION_START_CONNECTION";
    public static final String ACTION_DEVICE_CONNECTED = "com.acterics.healthmonitor.ACTION_DEVICE_CONNECTED";
    public static final String ACTION_DEVICE_CONNECTION_FAIL = "com.acterics.healthmonitor.ACTION_DEVICE_CONNECTION_FAIL";

    public static final String KEY_MESSAGE = "com.acterics.healthmonitor.KEY_MESSAGE";
    public static final String KEY_DEVICE = "com.acterics.healthmonitor.KEY_DEVICE";


    private BluetoothDevice device = null;
    private BluetoothAdapter adapter;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        adapter = BluetoothAdapter.getDefaultAdapter();
        if (device == null) {
            sendBroadcast(new Intent(CardioMonitorFragment.ACTION_LOST_CONNECTION));
        } else {

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(ACTION_START_CONNECTION)) {
            startService(new Intent(this, ConnectDeviceIntentService.class));
        }
        if (intent.getAction().equals(ACTION_DEVICE_CONNECTED)) {
            device = intent.getParcelableExtra(KEY_DEVICE);
        }
        if (intent.getAction().equals(ACTION_DEVICE_CONNECTION_FAIL)) {

        }

        return START_STICKY;
    }

}
