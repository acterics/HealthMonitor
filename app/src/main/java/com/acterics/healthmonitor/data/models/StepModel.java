package com.acterics.healthmonitor.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by oleg on 21.05.17.
 */

public class StepModel {

    @SerializedName("steps")
    private int steps;

    @SerializedName("timeFrom")
    private long timeFrom;

    @SerializedName("timeTo")
    private long timeTo;

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public long getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(long timeFrom) {
        this.timeFrom = timeFrom;
    }

    public long getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(long timeTo) {
        this.timeTo = timeTo;
    }
}
