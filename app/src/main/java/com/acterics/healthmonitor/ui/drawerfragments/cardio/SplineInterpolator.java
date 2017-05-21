package com.acterics.healthmonitor.ui.drawerfragments.cardio;

import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleg on 20.05.17.
 */
public class SplineInterpolator {
    List<DataPoint> pointExts;
    private final double[] mM;
    private SplineInterpolator(List<DataPoint> pointExts, double[] m) {
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
    public static SplineInterpolator createMonotoneCubicSpline(List<DataPoint> points) {
        if (points == null || points.size() < 2) {
//                throw new IllegalArgumentException("There must be at least two control "
//                        + "points and the arrays must be of equal length.");
            return null;
        }

        final int n = points.size();
        double[] d = new double[n - 1]; // could optimize this out
        double[] m = new double[n];

        // Compute slopes of secant lines between successive points.
        for (int i = 0; i < n - 1; i++) {
            double h = (points.get(i + 1).getX()) - (points.get(i).getX());
            if (h <= 0f) {
                throw new IllegalArgumentException("The control points must all "
                        + "have strictly increasing X values.");
            }
            d[i] = (points.get(i + 1).getY() - points.get(i).getY()) / h;
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
                double a = m[i] / d[i];
                double b = m[i + 1] / d[i];
                double h = (double) Math.hypot(a, b);
                if (h > 9f) {
                    double t = 3f / h;
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
    public double interpolate(double x) {
        // Handle the boundary cases.
        final int n = pointExts.size();
        if (Double.isNaN(x)) {
            return x;
        }
        if (x <= pointExts.get(0).getX()) {
            return pointExts.get(0).getY();
        }
        if (x >= pointExts.get(n - 1).getX()) {
            return pointExts.get(n - 1).getY();
        }

        // Find the index 'i' of the last point with smaller X.
        // We know this will be within the spline due to the boundary tests.
        int i = 0;
        while (x >= pointExts.get(i + 1).getX() ) {
            i += 1;
            if (x == pointExts.get(i).getX()) {
                return pointExts.get(i).getY();
            }
        }

        // Perform cubic Hermite spline interpolation.
        double h = pointExts.get(i + 1).getX() - pointExts.get(i).getX();
        double t = (x - pointExts.get(i).getX()) / h;
        return (pointExts.get(i).getY() * (1 + 2 * t) + h * mM[i] * t) * (1 - t) * (1 - t)
                + (pointExts.get(i + 1).getY() * (3 - 2 * t) + h * mM[i + 1] * (t - 1)) * t * t;
    }
}