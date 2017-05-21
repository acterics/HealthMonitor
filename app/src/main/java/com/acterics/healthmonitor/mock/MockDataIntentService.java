package com.acterics.healthmonitor.mock;

import android.app.IntentService;
import android.content.Intent;

import com.acterics.healthmonitor.ui.drawerfragments.cardio.CardioMonitorFragment;

import timber.log.Timber;

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
        Timber.e("onCreateView: start connecting");
//        StompClient stompClient = StompClient.clientOverWebsocket("ws://healsense-main.herokuapp.com/ws-api");
//
//        stompClient.connect(stompHeaders -> {
//            stompClient.subscribe("/queue/cardiogram/admin", CardiogramModel.class,
//                    (payload, stompHeaders1) -> {
//                        CardiogramModel model = (CardiogramModel) payload;
//                        for (double d: model.getValues()) {
//                            sendBroadcast(new Intent(ACTION_DATA).putExtra(EXTRA_DEVICE_DATA, d));
//                        }
//                    });
//            stompClient.send("/app/chat", new Gson().toJson(new Request(PreferenceUtils.getLastUserName(this))));
//        });
    }

    private static class Request {
        String name;
        public Request(String name) {
            this.name = name;
        }
    }

}
