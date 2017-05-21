package com.acterics.healthmonitor.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by oleg on 21.05.17.
 */

public class CardiogramModel {

    @SerializedName("startTime")
    private Long startTine;

    @SerializedName("heartRate")
    private Integer heartRate;

    @SerializedName("interval")
    private Integer interval;

    @SerializedName("values")
    private List<Double> values;

    public Long getStartTine() {
        return startTine;
    }

    public void setStartTine(Long startTine) {
        this.startTine = startTine;
    }

    public Integer getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(Integer heartRate) {
        this.heartRate = heartRate;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public List<Double> getValues() {
        return values;
    }

    public void setValues(List<Double> values) {
        this.values = values;
    }
}
