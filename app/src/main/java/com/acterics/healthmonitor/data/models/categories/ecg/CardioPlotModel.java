package com.acterics.healthmonitor.data.models.categories.ecg;

/**
 * Created by oleg on 12.05.17.
 */

public class CardioPlotModel {
    private int value;
    private long timestamp;
    public CardioPlotModel(int value) {
        this.value = value;
        this.timestamp = System.currentTimeMillis();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
