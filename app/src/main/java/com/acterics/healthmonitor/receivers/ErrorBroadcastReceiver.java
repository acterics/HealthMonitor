package com.acterics.healthmonitor.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.WindowManager;
import android.widget.Toast;

import com.acterics.healthmonitor.R;
import com.acterics.healthmonitor.utils.NavigationUtils;
import com.acterics.healthmonitor.utils.PreferenceUtils;

import cn.pedant.SweetAlert.SweetAlertDialog;
import timber.log.Timber;

/**
 * Created by oleg on 27.04.17.
 * Receiver that get errors all over the application.
 * Action depends on {@link ErrorBroadcastReceiver#EXTRA_ERROR_CODE}
 * @see ErrorCode
 *
 */

public class ErrorBroadcastReceiver extends BroadcastReceiver {

    public static final int USER_NOT_FOUND = 104;

    public static final String ACTION_ERROR = "com.acterics.healthmonitor.receivers.ACTION_ERROR";
    public static final String EXTRA_ERROR_MESSAGE = "com.acterics.healthmonitor.receivers.EXTRA_ERROR_MESSAGE";
    public static final String EXTRA_ERROR_CODE = "com.acterics.healthmonitor.receivers.EXTRA_ERROR_CODE";


    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra(EXTRA_ERROR_MESSAGE);
        ErrorCode code = (ErrorCode) intent.getSerializableExtra(EXTRA_ERROR_CODE);
        startActionFromCode(context, code, message);
    }

    public static void sendError(Context context, ErrorCode errorCode, Throwable th) {
        Timber.e("Received throwable", th);
        sendError(context, errorCode, th.getMessage());
    }

    public static void sendError(Context context, ErrorCode errorCode, String message) {
        Intent errorIntent = new Intent(ErrorBroadcastReceiver.ACTION_ERROR);
        errorIntent.putExtra(ErrorBroadcastReceiver.EXTRA_ERROR_CODE, errorCode);
        errorIntent.putExtra(ErrorBroadcastReceiver.EXTRA_ERROR_MESSAGE, message);
        context.sendBroadcast(errorIntent);
    }

    private void startActionFromCode(Context context, ErrorCode code, String message) {
        Timber.e("Received error: code: %s,\n\t message: %s.", code.toString(), message);
        switch (code) {
            case IGNORE:
                break;
            case TOAST:
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                break;
            case ALERT:
                showAlert(context, message);
                break;
            case UNAUTHORIZED:
                unauthorizedAction(context, message);
                break;
            case FATAL:
                fatalAction(context, message);
                break;
            default:
                throw new IllegalArgumentException("WRONG ERROR CODE: " + code);

        }
    }

    private void showAlert(Context context, String message) {
        SweetAlertDialog alert = new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(context.getString(R.string.error))
                .setContentText(message);

        alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alert.show();

    }

    private void unauthorizedAction(Context context, String message) {
        PreferenceUtils.clearPreference(context);
        NavigationUtils.toAuthorization(context);
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private void fatalAction(Context context, String message) {
        //TODO implement action

        showAlert(context, message);
    }
}
