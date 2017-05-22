package com.acterics.healthmonitor.utils;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by root on 22.05.17.
 */

public class KeyboardUtils {
    public static void hide(FragmentActivity activity) {
        View view = null;
        if (activity != null) {
            view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager inputManager =
                        (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                        0);
            }
        }
    }

    public static void show(FragmentActivity activity, View v) {
        InputMethodManager inputManager =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
    }


}
