package com.acterics.healthmonitor.data.models.categories.ecg;

/**
 * Created by oleg on 21.05.17.
 */

public class CardiogramServerModel {

    private Double time;
    private Double value;

    public CardiogramServerModel(Double time, Double value) {
        this.time = time;
        this.value = value;
    }
    public CardiogramServerModel() {

    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
