package com.acterics.healthmonitor.data.models.rest.responses;

import com.acterics.healthmonitor.data.models.categories.ecg.CardiogramServerModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by oleg on 21.05.17.
 */

public class CardiogramDataResponse {

    @SerializedName("points")
    private List<CardiogramServerModel> points;

    @SerializedName("pulse")
    private int pulse;

    public CardiogramDataResponse(List<CardiogramServerModel> points) {
        this.points = points;
    }

    public CardiogramDataResponse() {

    }

    public int getPulse() {
        return pulse;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }

    public List<CardiogramServerModel> getPoints() {
        return points;
    }

    public void setPoints(List<CardiogramServerModel> points) {
        this.points = points;
    }
}
