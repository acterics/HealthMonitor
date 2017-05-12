package com.acterics.healthmonitor;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by oleg on 12.05.17.
 */

public class ConnectDeviceIntentService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public ConnectDeviceIntentService() {
        super("Connect device");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }


}
