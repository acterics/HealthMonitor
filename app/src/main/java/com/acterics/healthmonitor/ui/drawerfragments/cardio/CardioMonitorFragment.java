package com.acterics.healthmonitor.ui.drawerfragments.cardio;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acterics.healthmonitor.R;
import com.acterics.healthmonitor.mock.MockDataIntentService;
import com.acterics.healthmonitor.services.CardioDeviceDataService;
import com.acterics.healthmonitor.ui.drawerfragments.GeneralFragment;
import com.androidplot.util.Redrawer;
import com.androidplot.xy.AdvancedLineAndPointRenderer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static android.app.Activity.RESULT_OK;

/**
 * Created by oleg on 12.05.17.
 */

public class CardioMonitorFragment extends Fragment {

    public static final String ACTION_DATA = "com.acterics.healthmonitor.ui.drawerfragments.ACTION_DATA";
    public static final String ACTION_LOST_CONNECTION = "com.acterics.healthmonitor.ui.drawerfragments.ACTION_LOST_CONNECTION";
    public static final String ACTION_ENABLE_BLUETOOTH = "com.acterics.healthmonitor.ui.drawerfragments.ACTION_ENABLE_BLUETOOTH";
    public static final String ACTION_UNAVAILABLE = "com.acterics.healthmonitor.ui.drawerfragments.ACTION_UNAVAILABLE";
    public static final String EXTRA_DEVICE_DATA = "com.acterics.healthmonitor.ui.drawerfragments.EXTRA_DEVICE_DATA";

    private static final int REQUEST_ENABLE_BLUETOOTH = 1;

    @BindView(R.id.loadingPanel) View loadingPanel;
    @BindView(R.id.plot) XYPlot plot;


    private AlertDialog.Builder builder;
    private Redrawer redrawer;
    private ECGModel ecgSeries;


    private BroadcastReceiver cardioDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Timber.i("onReceive");
            loadingPanel.setVisibility(View.GONE);
            switch (intent.getAction()) {
                case ACTION_ENABLE_BLUETOOTH:
                    turnOnBluetooth();
                    break;
                case ACTION_LOST_CONNECTION:
                    connectDevice();
                    break;
                case ACTION_UNAVAILABLE:
                    unavailable();
                    break;
                case ACTION_DATA:
                    ecgSeries.addVertex(intent.getIntExtra(EXTRA_DEVICE_DATA, 0));
                    break;
            }
            loadingPanel.setVisibility(View.GONE);
        }
    };

    private Dialog.OnClickListener onEnableBluetooth = (dialog, which) -> {
        Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH);
    };

    private Dialog.OnClickListener connectDevice = (dialog, which) -> {
        Timber.e("connectDevice");
        loadingPanel.setVisibility(View.VISIBLE);
        Intent startConnection = new Intent(getContext(), CardioDeviceDataService.class);
        startConnection.setAction(CardioDeviceDataService.ACTION_START_CONNECTION);
        getActivity().startService(startConnection);
    };



    private void turnOnBluetooth() {
        builder.setTitle(R.string.warning)
                .setMessage(R.string.message_enable_bluetooth)
                .setPositiveButton(R.string.enable, onEnableBluetooth)
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel())
                .setOnCancelListener(dialog -> onCancelDialog())
                .create()
                .show();

    }

    private void connectDevice() {
        builder.setTitle(R.string.warning)
                .setMessage(R.string.message_connect_device)
                .setPositiveButton(R.string.connect, connectDevice)
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.cancel())
                .setOnCancelListener(dialog -> onCancelDialog())
                .create()
                .show();

    }

    private void onCancelDialog() {
        getFragmentManager().beginTransaction()
                .replace(R.id.holder_content, new GeneralFragment())
                .commit();
    }

    private void unavailable() {
        builder.setTitle(R.string.error)
                .setMessage(R.string.error_cardio_monitoring_unavailable)
                .setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss())
                .setOnDismissListener(dialog -> onCancelDialog())
                .create()
                .show();
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cardio_monitor, container, false);
        ButterKnife.bind(this, view);
        builder = new AlertDialog.Builder(getContext(), R.style.DefaultDialog);
        loadingPanel.setVisibility(View.VISIBLE);

        initPlot();

//        Intent startCommunicationIntent = new Intent(getContext(), CardioDeviceDataService.class);
        Intent startCommunicationIntent = new Intent(getContext(), MockDataIntentService.class);
        startCommunicationIntent.setAction(CardioDeviceDataService.ACTION_START_COMMUNICATE);
        getActivity().startService(startCommunicationIntent);

        return view;
    }


    private void initPlot() {
        ecgSeries = new ECGModel(20);

        // add a new series' to the xyplot:
        MyFadeFormatter formatter =new MyFadeFormatter(40);
        formatter.setLegendIconEnabled(false);
        formatter.getLinePaint().setStyle(Paint.Style.FILL_AND_STROKE);
        plot.addSeries(ecgSeries, formatter);
        plot.setRangeBoundaries(-100, 100, BoundaryMode.FIXED);
        plot.setDomainBoundaries(0, 20, BoundaryMode.FIXED);

        // reduce the number of range labels
        plot.setLinesPerRangeLabel(3);

        // start generating ecg data in the background:
        ecgSeries.start(new WeakReference<>(plot.getRenderer(AdvancedLineAndPointRenderer.class)));

        redrawer = new Redrawer(plot, 30, true);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        IntentFilter cardioDataIntentFilter = new IntentFilter();
        cardioDataIntentFilter.addAction(ACTION_DATA);
        cardioDataIntentFilter.addAction(ACTION_LOST_CONNECTION);
        cardioDataIntentFilter.addAction(ACTION_ENABLE_BLUETOOTH);
        cardioDataIntentFilter.addAction(ACTION_UNAVAILABLE);
        getActivity().registerReceiver(cardioDataReceiver, cardioDataIntentFilter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().unregisterReceiver(cardioDataReceiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        loadingPanel.setVisibility(View.GONE);
        if (requestCode == REQUEST_ENABLE_BLUETOOTH && resultCode == RESULT_OK) {
            Intent startCommunicationIntent = new Intent(getContext(), CardioDeviceDataService.class);
            startCommunicationIntent.setAction(CardioDeviceDataService.ACTION_START_COMMUNICATE);
            getActivity().startService(startCommunicationIntent);
        } else {
            Timber.e("onActivityResult: something wrong");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public static class MyFadeFormatter extends AdvancedLineAndPointRenderer.Formatter {

        private int trailSize;

        public MyFadeFormatter(int trailSize) {
            this.trailSize = trailSize;
        }

        @Override
        public Paint getLinePaint(int thisIndex, int latestIndex, int seriesSize) {
            // offset from the latest index:
            int offset;
            if(thisIndex > latestIndex) {
                offset = latestIndex + (seriesSize - thisIndex);
            } else {
                offset =  latestIndex - thisIndex;
            }

            float scale = 255f / trailSize;
            int alpha = (int) (255 - (offset * scale));
            getLinePaint().setAlpha(alpha > 0 ? alpha : 0);
            return getLinePaint();
        }
    }


    public static class ECGModel implements XYSeries {


        private int latestIndex;
        private float latestPosition;
        private int seriesSize;
        private List<PointF> buffer;
        private int bufferSize;
        private Handler handler;
        private float currentPosition;
        private float step;
        private int bufferExtra = 5;
        private List<PointF> data;
        private boolean filled = false;
        private int pointsCount;

        private WeakReference<AdvancedLineAndPointRenderer> rendererRef;

        /**
         *
         * @param size Sample size contained within this model
         */
        public ECGModel(int size) {

            seriesSize = size;
            bufferSize = seriesSize / 10;
            buffer = new ArrayList<>(bufferSize);
            data = new ArrayList<>(size);
            handler = new Handler();
            latestPosition = 0;
            pointsCount = seriesSize * bufferExtra;
            step = (float)seriesSize / pointsCount;




        }

        public void addVertex(Number vertex) {
            if (buffer.size() >= bufferSize) {
                draw();
            } else {
                buffer.add(new PointF(currentPosition, vertex.floatValue()));
                currentPosition ++;
            }

        }


        private void draw() {
            SplineInterpolator interpolator = SplineInterpolator.createMonotoneCubicSpline(buffer);
            buffer.clear();

            int start = latestIndex;

            handler.post(new Runnable() {
                private int localIteration = 0;
                int condition = bufferSize * bufferExtra + start;
                @Override
                public void run() {
                    if (latestIndex < condition) {
                        if (latestIndex >= pointsCount) {
                            latestIndex = 0;
                            latestPosition = 0;
                            condition = bufferExtra - localIteration;
                            filled = true;
                            return;

                        }

                        if (filled) {
                            data.set(latestIndex, new PointF(latestPosition, interpolator.interpolate(latestPosition)));
                        } else {
                            data.add(new PointF(latestPosition, interpolator.interpolate(latestPosition)));
                        }

                        if (rendererRef.get() != null) {
                            rendererRef.get().setLatestIndex(latestIndex);
                        }
                        latestIndex++;
                        latestPosition += step;
                        localIteration++;
                        handler.postDelayed(this, 50);
                    }
                }
            });
        }

        public void start(final WeakReference<AdvancedLineAndPointRenderer> rendererRef) {
            this.rendererRef = rendererRef;
        }

        @Override
        public int size() {
            return pointsCount;
        }

        @Override
        public Number getX(int index) {
            return data.get(index).x;
        }

        @Override
        public Number getY(int index) {
            return data.get(index).y;
        }



        @Override
        public String getTitle() {
            return "Signal";
        }



    }

    @Override
    public void onStop() {
        super.onStop();
        redrawer.pause();
    }

    @Override
    public void onStart() {
        super.onStart();
        redrawer.start();
    }
}
