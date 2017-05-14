package com.acterics.healthmonitor.base;

import android.content.Context;

import com.acterics.healthmonitor.data.models.rest.responses.BaseResponse;
import com.acterics.healthmonitor.receivers.ErrorBroadcastReceiver;
import com.acterics.healthmonitor.receivers.ErrorCode;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by oleg on 14.05.17.
 */

public abstract class BaseCallback<R extends BaseResponse<R>> implements Callback<BaseResponse<R>> {

    private Context context;
    public BaseCallback(Context context) {
        this.context = context;
    }

    @Override
    public void onResponse(Call<BaseResponse<R>> call, Response<BaseResponse<R>> response) {
        if (response.isSuccessful()) {
            if (response.body() != null) {
                BaseResponse<R> responseBody = response.body();
                if (responseBody.getStatus() != 0) {
                    onSuccess(responseBody.getResponse());
                    return;
                } else {
                    ErrorBroadcastReceiver.sendError(context, ErrorCode.ALERT, responseBody.getMessage());
                }
            } else {
                ErrorBroadcastReceiver.sendError(context, ErrorCode.ALERT, "Response body = null!");
            }
        } else {
            ErrorBroadcastReceiver.sendError(context, ErrorCode.ALERT, "Unsuccessful request!");
        }
        onError();
    }

    @Override
    public void onFailure(Call<BaseResponse<R>> call, Throwable t) {
        ErrorBroadcastReceiver.sendError(context, ErrorCode.ALERT, t);
        onError();
    }

    public abstract void onSuccess(R body);
    public abstract void onError();
}
