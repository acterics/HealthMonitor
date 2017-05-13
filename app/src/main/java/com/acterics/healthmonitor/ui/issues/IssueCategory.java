package com.acterics.healthmonitor.ui.issues;

import android.support.annotation.DrawableRes;

import com.acterics.healthmonitor.R;

/**
 * Created by oleg on 13.05.17.
 */

public enum IssueCategory {

    HEART,
    STOMACH,
    HEAD,
    ETC;

    @DrawableRes
    public static int getDrawable(IssueCategory category) {
        switch (category) {
            case HEART:
                return R.drawable.ic_favorite_black_36dp;
            case STOMACH:
            case HEAD:
            case ETC:
                return R.drawable.ic_error_outline_black_36dp;
        }
        return -1;
    }
}
