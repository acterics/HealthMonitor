package com.acterics.healthmonitor.utils;

import android.content.Context;
import android.content.Intent;

import com.acterics.healthmonitor.ui.MainActivity;
import com.acterics.healthmonitor.ui.RoutingActivity;
import com.acterics.healthmonitor.ui.auth.AuthorizationActivity;

/**
 * Created by oleg on 13.05.17.
 */

public class NavigationUtils {
    private NavigationUtils() {

    }


    public static void toMain(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void toAuthorization(Context context, boolean newTask) {
        Intent intent = new Intent(context, AuthorizationActivity.class);
        if (newTask) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        context.startActivity(intent);
    }


    public static void toRouter(Context context) {
        Intent intent = new Intent(context, RoutingActivity.class);
        context.startActivity(intent);
    }


}
