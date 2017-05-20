package com.acterics.healthmonitor.ui.drawerfragments.complaint;

import android.support.annotation.DrawableRes;

import com.acterics.healthmonitor.R;

/**
 * Created by oleg on 13.05.17.
 */

public enum ComplaintCategory {

    HEART,
    STOMACH,
    HEAD,
    ETC;

    //TODO add icons
    @DrawableRes
    public static int getDrawable(int category) {
        switch (category) {
            case 0:
                return R.drawable.ic_favorite_black_36dp;
            case 1:
            case 2:
            case 3:
                return R.drawable.ic_error_outline_black_36dp;
            default:
                return -1;
        }
    }


}
