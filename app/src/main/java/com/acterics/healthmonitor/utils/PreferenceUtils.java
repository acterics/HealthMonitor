package com.acterics.healthmonitor.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.acterics.healthmonitor.data.models.UserModel;
import com.acterics.healthmonitor.data.models.rest.requests.BaseUserInfoRequest;
import com.acterics.healthmonitor.data.models.rest.responses.AuthResponse;
import com.google.gson.Gson;

/**
 * Created by oleg on 13.05.17.
 * Utility class for working with {@link SharedPreferences}
 */

public class PreferenceUtils {
    private static final String KEY_USER_TOKEN = "com.acterics.healthmonitor.utils.KEY_USER_TOKE";
    private static final String KEY_USER_ID = "com.acterics.healthmonitor.utils.KEY_USER_ID";
    private static final String KEY_USER_NAME = "com.acterics.healthmonitor.utils.KEY_USER_NAME";
    private static final String KEY_USER_IMAGE = "com.acterics.healthmonitor.utils.KEY_USER_IMAGE";
    private static final String KEY_USER_INFO = "com.acterics.healthmonitor.utils.KEY_USER_INFO";
    private static final String KEY_INIT_STATE = "com.acterics.healthmonitor.utils.KEY_INIT_STATE";

    private static final String KEY_LAST_USER_NAME = "com.acterics.healthmonitor.utils.KEY_LAST_USER_NAME";
    private static final String KEY_LAST_USER_AVATAR = "com.acterics.healthmonitor.utils.KEY_LAST_USER_AVATAR";
    private static final String KEY_LAST_USER_EMAIL = "com.acterics.healthmonitor.utils.KEY_LAST_USER_EMAIL";

    private static final String PREFERENCE_NAME = "HealthMonitorPrefs";

    private static final Gson gson = new Gson();

    private static SharedPreferences preferences = null;

    private PreferenceUtils() {

    }


    private static SharedPreferences getPreferences(Context context) {
        if (preferences == null) {
            preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
        return preferences;
    }


    /**
     * Shows if application started at first time
     * @param context for {@link PreferenceUtils#preferences} initialization if need
     * @return true if application first started, else false
     */
    public static boolean isInitState(Context context) {
        boolean initState = getPreferences(context).getBoolean(KEY_INIT_STATE, false);
        if (initState) {
            getPreferences(context)
                    .edit()
                    .putBoolean(KEY_INIT_STATE, false)
                    .apply();
        }
        return initState;
    }

    /**
     * Fill {@link PreferenceUtils#preferences} with authorization data.
     * @param context for {@link PreferenceUtils#preferences} initialization if need
     * @param body contains authorization data.
     */
    public static void authorize(Context context, AuthResponse body) {
        getPreferences(context)
                .edit()
                .putString(KEY_USER_TOKEN, body.getToken())
                .apply();
    }

    @Nullable
    public static String getUserToken(Context context) {
        return getPreferences(context).getString(KEY_USER_TOKEN, null);
    }

    /**
     * Clear all data from {@link PreferenceUtils#preferences}.
     * Use for logout action.
     * @param context for {@link PreferenceUtils#preferences} initialization if need
     */
    public static void clearPreference(Context context) {
        UserModel userModel = getUserModel(context);
        String lastUserName = userModel.getFirstName();
        String lastUserEmail = userModel.getEmail();
        String lastUserAvatar = userModel.getAvatar();
        getPreferences(context)
                .edit()
                .clear()
                .putString(KEY_LAST_USER_NAME, lastUserName)
                .putString(KEY_LAST_USER_EMAIL, lastUserEmail)
                .putString(KEY_LAST_USER_AVATAR, lastUserAvatar)
                .apply();
    }

    /**
     * Check current authorization state.
     * @param context for {@link PreferenceUtils#preferences} initialization if need
     * @return current authorization state.
     */
    public static boolean isAuthorized(Context context) {
        return getUserToken(context) != null;
    }


    /**
     * Method that fill {@link BaseUserInfoRequest} object.
     * Set {@link PreferenceUtils#KEY_USER_ID} and {@link PreferenceUtils#KEY_USER_TOKEN}
     * @param context for {@link PreferenceUtils#preferences} initialization if need
     * @param request fill object with data from {@link SharedPreferences}
     */
    @Deprecated
    public static void fillRequest(Context context, @NonNull BaseUserInfoRequest request) {
//        request.setId(getUserId(context));
        request.setToken(getUserToken(context));
    }

    public static void saveUserInfo(Context context, @NonNull UserModel userInfo) {
        getPreferences(context)
                .edit()
                .putString(KEY_USER_INFO, gson.toJson(userInfo))
                .apply();
    }

    public static UserModel getUserModel(Context context) {
        return gson.fromJson(getPreferences(context).getString(KEY_USER_INFO, "{}"), UserModel.class);
    }

    public static String getLastUserName(Context context) {
        return getPreferences(context).getString(KEY_LAST_USER_NAME, "Anon");
    }

    public static String getLastUserEmail(Context context) {
        return getPreferences(context).getString(KEY_LAST_USER_EMAIL, "");
    }

    public static String getLastUserAvatar(Context context) {
        return getPreferences(context).getString(KEY_LAST_USER_AVATAR, "");
    }





}
