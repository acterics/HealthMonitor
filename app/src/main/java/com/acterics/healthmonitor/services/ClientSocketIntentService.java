package com.acterics.healthmonitor.services;

import android.app.IntentService;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.util.Log;

import com.acterics.healthmonitor.receivers.ErrorBroadcastReceiver;
import com.acterics.healthmonitor.receivers.ErrorCode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import timber.log.Timber;

/**
 * Created by oleg on 12.05.17.
 */

public class ClientSocketIntentService extends IntentService {
    public static final String EXTRA_DEVICE = "com.acterics.healthmonitor.services.EXTRA_DEVICE";
    public static final String UUID = "00001101-0000-1000-8000-00805F9B34FB";

    public ClientSocketIntentService() {
        super("BluetoothSocketService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        BluetoothDevice device = intent.getParcelableExtra(EXTRA_DEVICE);
        BluetoothSocket socket;
        try {
            socket = device.createRfcommSocketToServiceRecord(java.util.UUID.fromString(UUID));
            socket.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = reader.readLine();
            while (line != null) {
                Timber.e("onDeviceConnected: %s", line);
            }
            reader.close();
            socket.close();
        } catch (IOException e) {
            ErrorBroadcastReceiver.sendError(this, ErrorCode.TOAST, e);
        }
    }
}
