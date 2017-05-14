package com.acterics.healthmonitor.mock;

import android.app.IntentService;
import android.content.Intent;

import java.util.Random;

import static com.acterics.healthmonitor.ui.drawerfragments.CardioMonitorFragment.ACTION_DATA;
import static com.acterics.healthmonitor.ui.drawerfragments.CardioMonitorFragment.EXTRA_DEVICE_DATA;

/**
 * Created by oleg on 12.05.17.
 */

public class MockDataIntentService extends IntentService {

    private static final int PERIOD = 20;

    public MockDataIntentService() {
        super("Mock cardio data");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Random random = new Random(System.currentTimeMillis());
        int value;
        int var1;
        int var2;
        int var3;
        for (long i = 0; i < 1000000000; i++) {
            var1 = random.nextInt(10);
            var2 = i % (PERIOD) == 0 ? 10 : i % (PERIOD) == 1 ? -10 : 1;
            var3 = random.nextInt(2) == 1 ? -1 : 1;
            value = var1 * var2 * var3;
            sendBroadcast(new Intent(ACTION_DATA).putExtra(EXTRA_DEVICE_DATA, value));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
