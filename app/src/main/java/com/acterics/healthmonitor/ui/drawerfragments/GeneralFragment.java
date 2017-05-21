package com.acterics.healthmonitor.ui.drawerfragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acterics.healthmonitor.R;
import com.acterics.healthmonitor.base.BaseFragment;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.LinkedList;
import java.util.Queue;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by oleg on 12.05.17.
 */

public class GeneralFragment extends BaseFragment {

    public static final String ACTION_DATA = "com.acterics.healthmonitor.ui.drawerfragments.ACTION_DATA";
    public static final String ACTION_LOST_CONNECTION = "com.acterics.healthmonitor.ui.drawerfragments.ACTION_LOST_CONNECTION";
    public static final String ACTION_ENABLE_BLUETOOTH = "com.acterics.healthmonitor.ui.drawerfragments.ACTION_ENABLE_BLUETOOTH";
    public static final String ACTION_UNAVAILABLE = "com.acterics.healthmonitor.ui.drawerfragments.ACTION_UNAVAILABLE";
    public static final String EXTRA_DEVICE_DATA = "com.acterics.healthmonitor.ui.drawerfragments.EXTRA_DEVICE_DATA";


    @BindView(R.id.graph_heart) LineChart graphHeart;
    private LineData data;
    private ILineDataSet set;



    private Queue<Double> buffersQueue;

    private Handler handler;

    private BroadcastReceiver cardioDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
            if (s.equals(ACTION_DATA)) {
                buffersQueue.add(intent.getDoubleExtra(EXTRA_DEVICE_DATA, 0));
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_general, container, false);
        ButterKnife.bind(this, view);

        buffersQueue = new LinkedList<>();

        handler = new Handler();

        graphHeart.setDragEnabled(false);
        graphHeart.setScaleEnabled(false);
        graphHeart.setTouchEnabled(false);

        XAxis xl = graphHeart.getXAxis();
        xl.setEnabled(false);

        YAxis leftAxis = graphHeart.getAxisLeft();
        leftAxis.setEnabled(false);



        YAxis rightAxis = graphHeart.getAxisRight();
        rightAxis.setEnabled(false);

        graphHeart.getLegend().setEnabled(false);
        graphHeart.getDescription().setEnabled(false);

        data = new LineData();

        set = createSet();
        data.addDataSet(set);

        data.addEntry(new Entry(0, 0), 0);

        graphHeart.setData(data);

        handler.post(new Runnable() {

            @Override
            public void run() {
                if (!buffersQueue.isEmpty()) {
                    data.addEntry(new Entry(set.getEntryCount(), buffersQueue.poll().floatValue()), 0);
                    data.notifyDataChanged();
                    graphHeart.notifyDataSetChanged();
                    graphHeart.setVisibleXRangeMaximum(200);
                    graphHeart.moveViewToX(data.getEntryCount());

                }
                handler.postDelayed(this, 50);
            }
        });

        return view;
    }



    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(cardioDataReceiver);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter cardioDataIntentFilter = new IntentFilter();
        cardioDataIntentFilter.addAction(ACTION_DATA);
        getActivity().registerReceiver(cardioDataReceiver, cardioDataIntentFilter);

    }


    private LineDataSet createSet() {

        LineDataSet result = new LineDataSet(null, "Dynamic Data");
        result.setAxisDependency(YAxis.AxisDependency.LEFT);
        result.setColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
        result.setLineWidth(1.5f);
        result.setDrawFilled(false);
        result.setDrawCircles(false);
        result.setFillAlpha(100);
        result.setFillColor(ResourcesCompat.getColor(getResources(), R.color.colorAccentLight, null));
        result.setDrawValues(false);
        return result;
    }


    @Override
    public boolean onBackPressed() {
        return false;
    }
}
