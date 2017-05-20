package com.acterics.healthmonitor.mock;

import android.app.IntentService;
import android.content.Intent;

import com.acterics.healthmonitor.receivers.ErrorBroadcastReceiver;
import com.acterics.healthmonitor.receivers.ErrorCode;
import com.acterics.healthmonitor.ui.drawerfragments.cardio.CardioMonitorFragment;

import java.util.Random;

import static com.acterics.healthmonitor.ui.drawerfragments.cardio.CardioMonitorFragment.ACTION_DATA;
import static com.acterics.healthmonitor.ui.drawerfragments.cardio.CardioMonitorFragment.EXTRA_DEVICE_DATA;

/**
 * Created by oleg on 12.05.17.
 * Debug only.
 * Intent service that generate mock cardiogram data and send in {@link Intent}
 * as extra {@link CardioMonitorFragment#EXTRA_DEVICE_DATA}
 * with action {@link CardioMonitorFragment#ACTION_DATA}.
 * Generate one in {@link MockDataIntentService#PERIOD} time 10x impulse.
 *
 */

public class MockDataIntentService extends IntentService {

    private static final int PERIOD = 100;

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
            var1 = random.nextInt(5);
            var2 = i % (PERIOD) == (PERIOD / 2) ? 20 : i % (PERIOD) == (PERIOD / 2) + 1 ? -20 : 1;
            var3 = random.nextInt(2) == 1 ? -1 : 1;
            value = var1 * var2 * var3;
            sendBroadcast(new Intent(ACTION_DATA).putExtra(EXTRA_DEVICE_DATA, value));
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                ErrorBroadcastReceiver.sendError(this, ErrorCode.ALERT, e);
            }
        }
    }

}
