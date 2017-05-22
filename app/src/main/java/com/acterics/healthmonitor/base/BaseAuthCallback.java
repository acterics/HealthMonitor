package com.acterics.healthmonitor.base;

import android.content.Context;
import android.support.annotation.NonNull;

import com.acterics.healthmonitor.data.RestClient;
import com.acterics.healthmonitor.data.models.categories.user.UserModel;
import com.acterics.healthmonitor.data.models.rest.responses.AuthResponse;
import com.acterics.healthmonitor.utils.PreferenceUtils;

/**
 * Created by oleg on 16.05.17.
 * Base callback implementation for all auth responses. Include request for user data
 */

public class BaseAuthCallback extends BaseCallback<AuthResponse> {


    private OnSuccessAuthListener onSuccessAuthListener = null;

    public BaseAuthCallback(Context context) {
        super(context);
    }

    public BaseAuthCallback(Context context, OnSuccessAuthListener onSuccessAuthListener) {
        this(context, null, onSuccessAuthListener);
    }

    public BaseAuthCallback(Context context, OnRequestErrorListener onRequestErrorListener, OnSuccessAuthListener onSuccessAuthListener) {
        super(context, onRequestErrorListener, true);
        this.onSuccessAuthListener = onSuccessAuthListener;
    }

    @Override
    public void onSuccess(@NonNull AuthResponse body) {
        PreferenceUtils.authorize(context, body);
        RestClient.getApiService().getUser("Bearer " + body.getToken())
                .enqueue(new BaseCallback<UserModel>(context) {
                    @Override
                    public void onSuccess(@NonNull UserModel body) {
                        PreferenceUtils.saveUserInfo(context, body);
                        if (onSuccessAuthListener != null) {
                            onSuccessAuthListener.onSuccessAuth(body);
                        }
                    }
                });
    }


    @FunctionalInterface
    public interface OnSuccessAuthListener {
        void onSuccessAuth(UserModel body);
    }


}
