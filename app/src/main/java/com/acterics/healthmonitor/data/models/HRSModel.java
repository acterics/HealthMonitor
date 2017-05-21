package com.acterics.healthmonitor.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by oleg on 21.05.17.
 */

public class HRSModel {
    @SerializedName("time")
    private long time;

    @SerializedName("value")
    private int value;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
