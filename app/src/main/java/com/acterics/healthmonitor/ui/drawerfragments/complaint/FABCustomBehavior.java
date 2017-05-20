package com.acterics.healthmonitor.ui.drawerfragments.complaint;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import timber.log.Timber;

/**
 * Created by oleg on 15.05.17.
 */

public class FABCustomBehavior extends FloatingActionButton.Behavior {

    public FABCustomBehavior(Context context , AttributeSet attrs) {
        super();
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        Timber.e("onNestedScroll: dy: %d unconsumed %d", dyConsumed, dxUnconsumed);
        //child -> Floating Action Button
        if (dyConsumed > 0) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            int fab_bottomMargin = layoutParams.bottomMargin;
            child.animate()
                    .translationY(child.getHeight() + fab_bottomMargin)
                    .setInterpolator(new LinearInterpolator())
                    .setDuration(150)
                    .start();
        } else if (dyConsumed < 0) {
            child.animate()
                    .translationY(0)
                    .setInterpolator(new LinearInterpolator())
                    .setDuration(150)
                    .start();
        }
    }


    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }
}
