package com.acterics.healthmonitor.ui.drawerfragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acterics.healthmonitor.R;
import com.acterics.healthmonitor.base.BaseCallback;
import com.acterics.healthmonitor.base.BaseFragment;
import com.acterics.healthmonitor.data.RestClient;
import com.acterics.healthmonitor.data.models.categories.activity.StepModel;
import com.acterics.healthmonitor.data.models.categories.activity.TemperatureModel;
import com.acterics.healthmonitor.data.models.categories.user.UserActivity;
import com.acterics.healthmonitor.utils.PreferenceUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Queue;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by oleg on 12.05.17.
 */

public class GeneralFragment extends BaseFragment {

    public static final String ACTION_DATA = "com.acterics.healthmonitor.ui.drawerfragments.ACTION_DATA";
    public static final String ACTION_PULSE = "com.acterics.healthmonitor.ui.drawerfragments.ACTION_PULSE";
    public static final String ACTION_LOST_CONNECTION = "com.acterics.healthmonitor.ui.drawerfragments.ACTION_LOST_CONNECTION";
    public static final String ACTION_ENABLE_BLUETOOTH = "com.acterics.healthmonitor.ui.drawerfragments.ACTION_ENABLE_BLUETOOTH";
    public static final String ACTION_UNAVAILABLE = "com.acterics.healthmonitor.ui.drawerfragments.ACTION_UNAVAILABLE";
    public static final String EXTRA_DEVICE_DATA = "com.acterics.healthmonitor.ui.drawerfragments.EXTRA_DEVICE_DATA";
    public static final String EXTRA_PULSE_DATA = "com.acterics.healthmonitor.ui.drawerfragments.EXTRA_PULSE_DATA";

    @BindView(R.id.chart_heart) LineChart graphHeart;
    @BindView(R.id.chart_steps) LineChart graphSteps;
    @BindView(R.id.chart_temperature) LineChart graphTemperature;
    @BindView(R.id.tv_pulse) TextView tvPulse;
    @BindView(R.id.tv_bedtime) TextView tvBedtime;
    private LineData data;
    private ILineDataSet set;



    private Queue<Double> buffersQueue;
    private UserActivity userActivity;

    private Handler handler;

    private BroadcastReceiver cardioDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();
            if (s.equals(ACTION_DATA)) {
                buffersQueue.add(intent.getDoubleExtra(EXTRA_DEVICE_DATA, 0));
            } else if (s.equals(ACTION_PULSE)) {
                tvPulse.setText(String.valueOf(intent.getIntExtra((EXTRA_PULSE_DATA), 0)));
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

        requestUserActivity();
        initGraph();

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!buffersQueue.isEmpty()) {
                    data.addEntry(new Entry(set.getEntryCount(), buffersQueue.poll().floatValue()), 0);
                    data.notifyDataChanged();
                    graphHeart.notifyDataSetChanged();
                    graphHeart.setVisibleXRangeMaximum(400);
                    graphHeart.moveViewToX(data.getEntryCount());

                }
                handler.postDelayed(this, 10);
            }
        });

        registerBroadcastReceiver();

        return view;
    }


    private void requestUserActivity() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        String date = dateFormat.format(new Date());
        RestClient.getApiService().getUserActivity(PreferenceUtils.getRequestUserToken(getContext()), date)
                .enqueue(new BaseCallback<UserActivity>(getContext()) {
                    @Override
                    public void onSuccess(@NonNull UserActivity body) {
                        userActivity = body;
                        onUserActivityLoaded();
                    }
                });
    }

    private void onUserActivityLoaded() {
        tvBedtime.setText(getString(R.string.bedtine, userActivity.getSleepHours()));
        LineData stepData = new LineData();
        ILineDataSet stepSet = createSet(R.color.colorAccent);
        stepData.addDataSet(stepSet);
        graphSteps.setData(stepData);

        int dx = 0;
        int i = 0;
        if (userActivity.getSteps() != null) {
            if (!userActivity.getSteps().isEmpty()) {
                dx = (int)(userActivity.getSteps().get(0).getTimeTo() - userActivity.getSteps().get(0).getTimeFrom());
            }
            for (StepModel model : userActivity.getSteps()) {
                Timber.e("onUserActivityLoaded: %d", model.getSteps());
                stepData.addEntry(new Entry(i, model.getSteps()), 0);
                i ++;
                dx = (int) (model.getTimeTo() - model.getTimeFrom());
            }

            stepData.notifyDataChanged();
            graphSteps.notifyDataSetChanged();
        }



        LineData temperatureData = new LineData();
        ILineDataSet temperatureSet = createSet(R.color.colorAccent);
        temperatureData.addDataSet(temperatureSet);
        graphTemperature.setData(temperatureData);
        i = 0;
        if (userActivity.getTemperature() != null) {
            for (TemperatureModel temperatureModel : userActivity.getTemperature()) {
                temperatureData.addEntry(new Entry(i, temperatureModel.getValue()), 0);
                i++;
            }
            temperatureData.notifyDataChanged();
            graphTemperature.notifyDataSetChanged();


        }

    }

    private void initGraph() {


        graphHeart.setDragEnabled(false);
        graphHeart.setScaleEnabled(false);
        graphHeart.setTouchEnabled(false);
        graphHeart.getXAxis().setEnabled(false);
        graphHeart.getAxisLeft().setEnabled(false);
        graphHeart.getAxisRight().setEnabled(false);
        graphHeart.getLegend().setEnabled(false);
        graphHeart.getDescription().setEnabled(false);

        graphSteps.setDragEnabled(false);
        graphSteps.setScaleEnabled(false);
        graphSteps.setTouchEnabled(false);
        graphSteps.getXAxis().setEnabled(false);
        graphSteps.getAxisLeft().setEnabled(false);
        graphSteps.getAxisRight().setEnabled(false);
        graphSteps.getLegend().setEnabled(false);
        graphSteps.getDescription().setEnabled(false);

        graphTemperature.setDragEnabled(false);
        graphTemperature.setScaleEnabled(false);
        graphTemperature.setTouchEnabled(false);
        graphTemperature.getXAxis().setEnabled(false);
        graphTemperature.getAxisLeft().setEnabled(false);
        graphTemperature.getAxisRight().setEnabled(false);
        graphTemperature.getLegend().setEnabled(false);
        graphTemperature.getDescription().setEnabled(false);

        data = new LineData();

        set = createSet(R.color.colorAccent);
        data.addDataSet(set);

        data.addEntry(new Entry(0, 0), 0);
        graphHeart.setData(data);
    }



    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(cardioDataReceiver);
        handler.removeCallbacksAndMessages(null);
    }

    private void registerBroadcastReceiver() {
        IntentFilter cardioDataIntentFilter = new IntentFilter();
        cardioDataIntentFilter.addAction(ACTION_DATA);
        cardioDataIntentFilter.addAction(ACTION_PULSE);
        getActivity().registerReceiver(cardioDataReceiver, cardioDataIntentFilter);
    }




    private LineDataSet createSet(@ColorRes int color) {

        LineDataSet result = new LineDataSet(null, "Dynamic Data");
        result.setAxisDependency(YAxis.AxisDependency.LEFT);
        result.setColor(ResourcesCompat.getColor(getResources(), color, null));
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
