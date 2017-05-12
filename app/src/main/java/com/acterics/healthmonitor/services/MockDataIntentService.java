package com.acterics.healthmonitor.services;

import android.app.IntentService;
import android.content.Intent;

import java.util.Random;

import static com.acterics.healthmonitor.drawerfragments.CardioMonitorFragment.ACTION_DATA;
import static com.acterics.healthmonitor.drawerfragments.CardioMonitorFragment.EXTRA_DEVICE_DATA;

/**
 * Created by oleg on 12.05.17.
 */

public class MockDataIntentService extends IntentService {

    public MockDataIntentService() {
        super("Mock cardio data");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Random random = new Random();
        int value;
        for (long i = 0; i < 1000000000; i++) {
            if (i % 100 == 0) {
                value = random.nextInt(200);
                sendBroadcast(new Intent(ACTION_DATA).putExtra(EXTRA_DEVICE_DATA, value));
            }
        }
    }

}
