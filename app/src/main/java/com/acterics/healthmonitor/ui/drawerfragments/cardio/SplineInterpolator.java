package com.acterics.healthmonitor.ui.drawerfragments.cardio;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleg on 20.05.17.
 */
class SplineInterpolator {
    List<PointF> pointExts;
    private final float[] mM;

    private SplineInterpolator(List<PointF> pointExts, float[] m) {
        this.pointExts = new ArrayList<>();
        this.pointExts.addAll(pointExts);
        mM = m;
    }

    /**
     * Creates a monotone cubic spline from a given set of control points.
     *
     * The spline is guaranteed to pass through each control point exactly. Moreover, assuming the control points are
     * monotonic (Y is non-decreasing or non-increasing) then the interpolated values will also be monotonic.
     *
     * This function uses the Fritsch-Carlson method for computing the spline parameters.
     * http://en.wikipedia.org/wiki/Monotone_cubic_interpolation
     *
     * @param points
     *            The X and Y components of the control points, strictly increasing.
     * @return
     *
     * @throws IllegalArgumentException
     *             if the X or Y arrays are null, have different lengths or have fewer than 2 values.
     */
    public static SplineInterpolator createMonotoneCubicSpline(List<PointF> points) {
        if (points == null || points.size() < 2) {
//                throw new IllegalArgumentException("There must be at least two control "
//                        + "points and the arrays must be of equal length.");
            return null;
        }

        final int n = points.size();
        float[] d = new float[n - 1]; // could optimize this out
        float[] m = new float[n];

        // Compute slopes of secant lines between successive points.
        for (int i = 0; i < n - 1; i++) {
            float h = points.get(i + 1).x - points.get(i).x;
            if (h <= 0f) {
                throw new IllegalArgumentException("The control points must all "
                        + "have strictly increasing X values.");
            }
            d[i] = (points.get(i + 1).y - points.get(i).y) / h;
        }

        // Initialize the tangents as the average of the secants.
        m[0] = d[0];
        for (int i = 1; i < n - 1; i++) {
            m[i] = (d[i - 1] + d[i]) * 0.5f;
        }
        m[n - 1] = d[n - 2];

        // Update the tangents to preserve monotonicity.
        for (int i = 0; i < n - 1; i++) {
            if (d[i] == 0f) { // successive Y values are equal
                m[i] = 0f;
                m[i + 1] = 0f;
            } else {
                float a = m[i] / d[i];
                float b = m[i + 1] / d[i];
                float h = (float) Math.hypot(a, b);
                if (h > 9f) {
                    float t = 3f / h;
                    m[i] = t * a * d[i];
                    m[i + 1] = t * b * d[i];
                }
            }
        }
        return new SplineInterpolator(points, m);
    }

    /**
     * Interpolates the value of Y = f(X) for given X. Clamps X to the domain of the spline.
     *
     * @param x
     *            The X value.
     * @return The interpolated Y = f(X) value.
     */
    public float interpolate(float x) {
        // Handle the boundary cases.
        final int n = pointExts.size();
        if (Float.isNaN(x)) {
            return x;
        }
        if (x <= pointExts.get(0).x) {
            return pointExts.get(0).y;
        }
        if (x >= pointExts.get(n - 1).x) {
            return pointExts.get(n - 1).y;
        }

        // Find the index 'i' of the last point with smaller X.
        // We know this will be within the spline due to the boundary tests.
        int i = 0;
        while (x >= pointExts.get(i + 1).x) {
            i += 1;
            if (x == pointExts.get(i).x) {
                return pointExts.get(i).y;
            }
        }

        // Perform cubic Hermite spline interpolation.
        float h = pointExts.get(i + 1).x - pointExts.get(i).x;
        float t = (x - pointExts.get(i).x) / h;
        return (pointExts.get(i).y * (1 + 2 * t) + h * mM[i] * t) * (1 - t) * (1 - t)
                + (pointExts.get(i + 1).y * (3 - 2 * t) + h * mM[i + 1] * (t - 1)) * t * t;
    }
}
