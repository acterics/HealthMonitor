package com.acterics.healthmonitor.utils;

import android.content.Context;
import android.content.Intent;

import com.acterics.healthmonitor.data.models.rest.responses.AuthResponse;
import com.acterics.healthmonitor.mock.MockDataIntentService;
import com.acterics.healthmonitor.services.CardioDeviceDataService;
import com.acterics.healthmonitor.ui.MainActivity;
import com.acterics.healthmonitor.ui.RoutingActivity;
import com.acterics.healthmonitor.ui.auth.AuthorizationActivity;

/**
 * Created by oleg on 13.05.17.
 * Utility class for application navigation.
 * Contains navigation method for every activity.
 */

public class NavigationUtils {
    private NavigationUtils() {

    }


    public static void toMain(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    /**
     * Navigate to {@link AuthorizationActivity}
     * @param context for invoking {@link Context#startActivity(Intent)} method
     */
    public static void toAuthorization(Context context) {
        Intent intent = new Intent(context, AuthorizationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void succesAuthorization(Context context, AuthResponse body) {
        PreferenceUtils.authorize(context, body);
        NavigationUtils.startListenCardion(context);
        NavigationUtils.toMain(context);
    }

    public static void logout(Context context) {

    }


    public static void toRouter(Context context) {
        Intent intent = new Intent(context, RoutingActivity.class);
        context.startActivity(intent);
    }


    public static void startListenCardion(Context context) {
        Intent startCommunicationIntent = new Intent(context, MockDataIntentService.class);
        startCommunicationIntent.setAction(CardioDeviceDataService.ACTION_START_COMMUNICATE);
        context.startService(startCommunicationIntent);
    }


}
