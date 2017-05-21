package com.acterics.healthmonitor.mock;

import android.app.IntentService;
import android.content.Intent;

import com.acterics.healthmonitor.R;
import com.acterics.healthmonitor.data.models.CardiogramServerModel;
import com.acterics.healthmonitor.data.models.UserModel;
import com.acterics.healthmonitor.data.models.rest.requests.CardiogramDataRequest;
import com.acterics.healthmonitor.stompclient.StompClient;
import com.acterics.healthmonitor.stompclient.listener.WebScoketErrorListener;
import com.acterics.healthmonitor.stompclient.listener.WebSocketCloseListener;
import com.acterics.healthmonitor.utils.PreferenceUtils;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

import static com.acterics.healthmonitor.ui.drawerfragments.GeneralFragment.ACTION_DATA;
import static com.acterics.healthmonitor.ui.drawerfragments.GeneralFragment.EXTRA_DEVICE_DATA;


/**
 * Created by oleg on 12.05.17.
 * Debug only.
 * Intent service that generate mock cardiogram data and send in {@link Intent}
 * as extra {@link com.acterics.healthmonitor.ui.drawerfragments.GeneralFragment#EXTRA_DEVICE_DATA}
 * with action {@link com.acterics.healthmonitor.ui.drawerfragments.GeneralFragment#ACTION_DATA}.
 * Generate one in {@link MockDataIntentService#PERIOD} time 10x impulse.
 *
 */

public class MockDataIntentService extends IntentService {

    private static final int PERIOD = 100;
    private List<CardiogramServerModel> data;
    private Gson gson;
    private StompClient stompClient;

    public MockDataIntentService() {
        super("Mock cardio data");
        data = new ArrayList<>(9000);
        gson = new Gson();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Timber.e("onCreateView: start connecting");
        stompClient = StompClient.clientOverWebsocket("ws://healsense-main.herokuapp.com/ws-api");
        initFile();

        UserModel user = PreferenceUtils.getUserModel(getApplicationContext());
        Timber.e("onHandleIntent: %s", user.getUserId());
        stompClient.connect(stompHeaders -> {
            Timber.e("onHandleIntent: connected");
            stompClient.subscribe("/queue/cardiogram/" + user.getUserId(), CardiogramDataRequest.class,
                    (payload, stompHeaders1) -> {
                        CardiogramDataRequest model = (CardiogramDataRequest) payload;
                        Timber.d("onHandleIntent: %s", payload);
                        for (CardiogramServerModel serverModel: model.getPoints()) {
                            sendBroadcast(new Intent(ACTION_DATA).putExtra(EXTRA_DEVICE_DATA, serverModel.getValue()));
                        }


                    });
            Timber.e("onHandleIntent: subscribed");
            stompClient.send("/app/connect/" + user.getUserId(), gson.toJson(new Request(PreferenceUtils.getLastUserName(this))));

            int bufferSize = 65;
            new Thread(() -> {
                for (int offset = 0; offset < data.size(); offset += bufferSize) {
                    List<CardiogramServerModel> buffer;
                    if (offset + bufferSize >= data.size()) {
                        offset = 0;
                    }
                    buffer = data.subList(offset, offset + bufferSize);
                    if (stompClient.isConnected()) {
                        stompClient.send("/app/mobile/user/" + user.getUserId(), gson.toJson(new CardiogramDataRequest(buffer)));
                    }
                    try {
                        Thread.sleep(200);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }).start();
        });

        stompClient.setWebScoketErrorListener(new WebScoketErrorListener() {
            @Override
            public void onError(Exception ex) throws Exception {
                Timber.e("onError: %s", ex.getMessage(), ex);
                ex.printStackTrace();
            }
        });
        
        stompClient.setWebSocketCloseListener(new WebSocketCloseListener() {
            @Override
            public void onClose(int code, String reason, boolean remote) throws Exception {
                Timber.e("onClose: %s", reason);
            }
        });



    }


    private void initFile() {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.cardiodata)));
            String line = bufferedReader.readLine();
            while (line != null) {
                String[] values = line.split(" ");
                data.add(new CardiogramServerModel(Double.parseDouble(values[0]), Double.parseDouble(values[1])));
                line = bufferedReader.readLine();
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public static class Request {
        String name;
        public Request(String name) {
            this.name = name;
        }
    }

}
