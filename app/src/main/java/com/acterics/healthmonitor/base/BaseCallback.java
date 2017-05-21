package com.acterics.healthmonitor.base;

import android.content.Context;
import android.support.annotation.NonNull;

import com.acterics.healthmonitor.data.models.rest.responses.BaseResponse;
import com.acterics.healthmonitor.receivers.ErrorBroadcastReceiver;
import com.acterics.healthmonitor.receivers.ErrorCode;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by oleg on 14.05.17.
 *
 * Base class for all application server request callbacks.
 * Check possible errors and notify {@link ErrorBroadcastReceiver}
 * Only one method you need to implement {@link BaseCallback#onSuccess(R)}
 *
 * @param <R> {@link BaseResponse#response} class
 */

public abstract class BaseCallback<R> implements Callback<BaseResponse<R>> {

    protected Context context;
    protected OnRequestErrorListener onRequestErrorListener = null;
    protected boolean handle = true;


    public BaseCallback(Context context) {
        this.context = context;
    }
    public BaseCallback(Context context, OnRequestErrorListener onRequestErrorListener, boolean handle) {
        this(context);
        this.onRequestErrorListener = onRequestErrorListener;
        this.handle = handle;
    }

    @Override
    public void onResponse(Call<BaseResponse<R>> call, Response<BaseResponse<R>> response) {
        ErrorCode errorCode;
        String message;
        int serverErrorCode = -1;
        if (response.isSuccessful()) {
            if (response.body() != null) {
                BaseResponse<R> responseBody = response.body();
                if (responseBody.getStatus() == 0) {
                    onSuccess(responseBody.getResponse());
                    return;
                } else {
                    serverErrorCode = responseBody.getStatus();
                    errorCode = handleStatus(responseBody.getStatus());
                    message = responseBody.getMessage();
                }
            } else {
                errorCode = ErrorCode.ALERT;
                message = "Response body = null!";
            }
        } else {
            errorCode = ErrorCode.ALERT;
            message = "Unsuccessful request!";
        }
        
        onError(context, message, errorCode, serverErrorCode);
    }

    @Override
    public void onFailure(Call<BaseResponse<R>> call, Throwable t) {
        ErrorBroadcastReceiver.sendError(context, ErrorCode.ALERT, t);
    }

    public abstract void onSuccess(@NonNull R body);

    public void onError(Context context, String message, ErrorCode errorCode, int serverErrorCode) {
        Timber.e("onError: %s, %s, %d", message, errorCode, serverErrorCode);
        if (onRequestErrorListener != null) {
            onRequestErrorListener.onRequestError(errorCode, serverErrorCode);
        }
        if (handle) {
            Timber.e("onError: send error");
            ErrorBroadcastReceiver.sendError(context, errorCode, message);
        }
        //By default not implemented
    }


    private ErrorCode handleStatus(int status) {
        if (status / 100 == 1) {
            return ErrorCode.TOAST;
        }
        if (status / 100 == 4) {
            if (status == 403) {
                return ErrorCode.UNAUTHORIZED;
            }
            return ErrorCode.ALERT;
        }
        if (status / 100 == 5) {
            return ErrorCode.TOAST;
        }
        return ErrorCode.IGNORE;
    }

    @FunctionalInterface
    public interface OnRequestErrorListener {
        void onRequestError(ErrorCode errorCode, int serverError);
    }
}
