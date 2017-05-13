package com.acterics.healthmonitor.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;

/**
 * Created by oleg on 13.05.17.
 */

public class PreferenceUtils {
    private static final String KEY_USER_TOKEN = "com.acterics.healthmonitor.utils.KEY_USER_TOKE";
    private static final String KEY_USER_ID = "com.acterics.healthmonitor.utils.KEY_USER_ID";
    private static final String KEY_USER_NAME = "com.acterics.healthmonitor.utils.KEY_USER_NAME";
    private static final String KEY_USER_IMAGE = "com.acterics.healthmonitor.utils.KEY_USER_IMAGE";

    private static final String PREFERENCE_NAME = "HealthMonitorPrefs";


    private static SharedPreferences preferences = null;

    private PreferenceUtils() {

    }


    private static SharedPreferences getPreferences(Context context) {
        if (preferences == null) {
            preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
        return preferences;
    }

    @Nullable
    public static String getUserToken(Context context) {
        return getPreferences(context).getString(KEY_USER_TOKEN, null);
    }

    @Nullable
    public static String getUserName(Context context) {
        return getPreferences(context).getString(KEY_USER_NAME, null);
    }

    @Nullable
    public static String getUserImage(Context context) {
        return getPreferences(context).getString(KEY_USER_IMAGE, null);
    }

    public static Long getUserId(Context context) {
        return getPreferences(context).getLong(KEY_USER_ID, -1);
    }

    public static void clearPreference(Context context) {
        getPreferences(context)
                .edit()
                .clear()
                .apply();
    }

    public static boolean isAuthorizad(Context context) {
        return getUserToken(context) != null;
    }





}
