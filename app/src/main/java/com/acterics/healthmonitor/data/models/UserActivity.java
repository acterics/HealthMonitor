package com.acterics.healthmonitor.data.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by oleg on 21.05.17.
 */

public class UserActivity {

    @SerializedName("date")
    private String date;

    @SerializedName("hrs")
    private List<HRSModel> hrs;

    @SerializedName("sleepHours")
    private int sleepHours;

    @SerializedName("steps")
    private List<StepModel> steps;

    @SerializedName("temperature")
    private List<TemperatureModel> temperature;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<HRSModel> getHrs() {
        return hrs;
    }

    public void setHrs(List<HRSModel> hrs) {
        this.hrs = hrs;
    }

    public int getSleepHours() {
        return sleepHours;
    }

    public void setSleepHours(int sleepHours) {
        this.sleepHours = sleepHours;
    }

    public List<StepModel> getSteps() {
        return steps;
    }

    public void setSteps(List<StepModel> steps) {
        this.steps = steps;
    }

    public List<TemperatureModel> getTemperature() {
        return temperature;
    }

    public void setTemperature(List<TemperatureModel> temperature) {
        this.temperature = temperature;
    }
}
