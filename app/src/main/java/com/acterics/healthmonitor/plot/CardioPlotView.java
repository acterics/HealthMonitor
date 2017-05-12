package com.acterics.healthmonitor.plot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.View;

import com.acterics.healthmonitor.R;
import com.acterics.healthmonitor.data.models.CardioPlotModel;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by oleg on 12.05.17.
 */

public class CardioPlotView extends View {
    private static final long DEFAULT_TIME_RANGE = 5000;
    private static final float DEFAULT_POINTER_POSITION_FACTOR = 0.8f;


    private long timeRange;
    private float plotPointerPositionFactor;
    private int plotPointsCount;
    private long plotStart;
    private long plotEnd;
    private Queue<CardioPlotModel> queue;
    private int maxValue;

    private final Paint borderPaint = new Paint();
    private final Paint plotPaint = new Paint();
    private final Path plotPath = new Path();

    public CardioPlotView(Context context) {
        super(context);
        init();
    }

    public CardioPlotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CardioPlotView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CardioPlotView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        queue = new LinkedList<>();
        timeRange = DEFAULT_TIME_RANGE;
        plotPointerPositionFactor = DEFAULT_POINTER_POSITION_FACTOR;
        plotStart = 0;
        plotEnd = 0;
        maxValue = 0;
        plotPointsCount = (int) (timeRange * plotPointerPositionFactor) * 10;

        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(Color.GRAY);

        plotPaint.setStyle(Paint.Style.STROKE);
        plotPaint.setStrokeWidth(3);
        plotPaint.setColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        drawPlot(canvas, width, height);

        super.onDraw(canvas);

    }


    private void drawPlotBorders(Canvas canvas) {
//        canvas.drawLine();
    }

    private void drawPlot(Canvas canvas, int width, int height) {
        plotPath.reset();
        if (!queue.isEmpty()) {
            plotPath.moveTo(getXPosition(queue.element(), width), getYPosition(queue.element(), height));
            for (CardioPlotModel model : queue) {
                plotPath.lineTo(getXPosition(model, width), getYPosition(model, height));
            }
        }
        canvas.drawPath(plotPath, plotPaint);

    }

    private float getXPosition(CardioPlotModel model, int width) {
        return (float)((model.getTimestamp() - plotStart) / timeRange) * width;
    }

    private float getYPosition(CardioPlotModel model, int height) {
        return (float)(model.getValue() / maxValue) * height;
    }

    public void addValue(CardioPlotModel value) {
//        Timber.e("addValue: %d", value.getValue());
        queue.add(value);
        plotEnd = value.getTimestamp();
        plotStart = plotEnd - timeRange;
        if (queue.size() > plotPointsCount) {
            queue.poll();
        }
        if (value.getValue() > maxValue) {
            maxValue = value.getValue();
        }
        invalidate();
    }
}
