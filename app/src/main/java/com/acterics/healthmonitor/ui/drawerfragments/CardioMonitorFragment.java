package com.acterics.healthmonitor.ui.drawerfragments;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acterics.healthmonitor.R;
import com.acterics.healthmonitor.mock.MockDataIntentService;
import com.acterics.healthmonitor.services.CardioDeviceDataService;
import com.androidplot.util.Redrawer;
import com.androidplot.xy.AdvancedLineAndPointRenderer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.lang.ref.WeakReference;

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

    private SimpleXYSeries series;
    private long lastTime;
//    @BindView(R.id.cardio_plot_view) CardioPlotView cardioPlotView;

    private AlertDialog.Builder builder;
    private Redrawer redrawer;


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
//                    if (series.size() > 50) {
//                        series.removeLast();
//                    }
//                    for (int i = 0; i < 100; i++) {
//                        series.addFirst();
//                    }
//                    series.addFirst(System.currentTimeMillis(), intent.getIntExtra(EXTRA_DEVICE_DATA, 0));
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
    ECGModel ecgSeries;

    private void initPlot() {
        series = new SimpleXYSeries("Data");
//        MyFadeFormatter formatter = new MyFadeFormatter(50);
//        formatter.getFillPaint().setColor(Color.TRANSPARENT);
//        formatter.getLinePaint().setColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
//        formatter.setInterpolationParams(
//                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));
//        formatter.getVertexPaint().setColor(Color.TRANSPARENT);
//
//        plot.addSeries(series, formatter);
//        plot.setRangeBoundaries(-100, 100, BoundaryMode.FIXED);



        ecgSeries = new ECGModel(100);

        // add a new series' to the xyplot:
        MyFadeFormatter formatter =new MyFadeFormatter(90);
        formatter.setLegendIconEnabled(false);
        plot.addSeries(ecgSeries, formatter);
        plot.setRangeBoundaries(-100, 100, BoundaryMode.FIXED);
        plot.setDomainBoundaries(0, 100, BoundaryMode.FIXED);

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
        private int seriesPoolSize = 4;
        private int seriesSize;
        private Number[] data;

        private WeakReference<AdvancedLineAndPointRenderer> rendererRef;

        /**
         *
         * @param size Sample size contained within this model
         */
        public ECGModel(int size) {
            seriesSize = size;
            data = new Number[seriesSize];



        }

        public void addVertex(Number vertex) {

            if (latestIndex >= seriesSize) {
                latestIndex = 0;
            }

            data[latestIndex] = vertex;

            if(latestIndex < seriesSize - 1) {
                // null out the point immediately following i, to disable
                // connecting i and i+1 with a line:
                data[latestIndex +1] = null;
            }

            if(rendererRef.get() != null) {
                rendererRef.get().setLatestIndex(latestIndex);

            }
            latestIndex++;
        }

        public void start(final WeakReference<AdvancedLineAndPointRenderer> rendererRef) {
            this.rendererRef = rendererRef;
        }

        @Override
        public int size() {
            return data.length;
        }

        @Override
        public Number getX(int index) {
            return index;
        }

        @Override
        public Number getY(int index) {
            return data[index];
        }

        public void setY(Number y, int index) {
            data[index] = y;
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
