package com.acterics.healthmonitor.ui.drawerfragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acterics.healthmonitor.R;
import com.acterics.healthmonitor.mock.MockDataIntentService;
import com.acterics.healthmonitor.services.CardioDeviceDataService;
import com.acterics.healthmonitor.ui.drawerfragments.cardio.SplineInterpolator;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.acterics.healthmonitor.ui.drawerfragments.cardio.CardioMonitorFragment.ACTION_DATA;
import static com.acterics.healthmonitor.ui.drawerfragments.cardio.CardioMonitorFragment.EXTRA_DEVICE_DATA;

/**
 * Created by oleg on 12.05.17.
 */

public class GeneralFragment extends Fragment {

    @BindView(R.id.graph_heart) GraphView graphHeart;

    private LineGraphSeries<DataPoint> series;

    private Queue<DataPoint> buffersQueue;

    private int i = 0;
    private int bufferIteration = 0;
    private List<DataPoint> buffer;
    private int bufferSize = 20;
    private int interpolatedBufferSize = 40;
    private Handler handler;
    private SplineInterpolator interpolator;

    private BroadcastReceiver cardioDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Timber.i("onReceive");
            switch (intent.getAction()) {
                case ACTION_DATA:
                    buffersQueue.add(new DataPoint(i, intent.getDoubleExtra(EXTRA_DEVICE_DATA, 0)));
                    i++;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_general, container, false);
        ButterKnife.bind(this, view);

        buffer = new LinkedList<>();
        buffersQueue = new LinkedList<>();


        handler = new Handler();


        graphHeart.getViewport().setXAxisBoundsManual(true);
        graphHeart.getViewport().setMinY(-100);
        graphHeart.getViewport().setMaxY(100);
        graphHeart.getViewport().setDrawBorder(false);
        graphHeart.getViewport().setMaxX(50);

        graphHeart.getGridLabelRenderer().setVerticalLabelsVisible(false);
        graphHeart.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graphHeart.getGridLabelRenderer().setGridColor(Color.TRANSPARENT);
        graphHeart.getLegendRenderer().setVisible(false);
        graphHeart.getLegendRenderer().setTextColor(Color.TRANSPARENT);

        series = new LineGraphSeries<>(new DataPoint[] {new DataPoint(0, 1)});

        series.setDrawBackground(true);
        series.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAccentLight, null));
        series.setColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
        graphHeart.addSeries(series);




        Intent startCommunicationIntent = new Intent(getContext(), MockDataIntentService.class);
        startCommunicationIntent.setAction(CardioDeviceDataService.ACTION_START_COMMUNICATE);
        getActivity().startService(startCommunicationIntent);



        handler.post(new Runnable() {
            @Override
            public void run() {
                if (!buffersQueue.isEmpty()) {
                    series.appendData(buffersQueue.poll(), true, 100);
                }
                handler.postDelayed(this, 50);
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        IntentFilter cardioDataIntentFilter = new IntentFilter();
        cardioDataIntentFilter.addAction(ACTION_DATA);
        getActivity().registerReceiver(cardioDataReceiver, cardioDataIntentFilter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().unregisterReceiver(cardioDataReceiver);
    }


}
