package com.acterics.healthmonitor.data.models.rest.requests;

import com.acterics.healthmonitor.data.models.CardiogramServerModel;

import java.util.List;

/**
 * Created by oleg on 21.05.17.
 */

public class CardiogramDataRequest {

    private List<CardiogramServerModel> points;

    public CardiogramDataRequest(List<CardiogramServerModel> points) {
        this.points = points;
    }

    public CardiogramDataRequest() {

    }

    public List<CardiogramServerModel> getPoints() {
        return points;
    }

    public void setPoints(List<CardiogramServerModel> points) {
        this.points = points;
    }
}
