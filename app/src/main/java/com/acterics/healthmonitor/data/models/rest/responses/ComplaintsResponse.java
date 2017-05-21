package com.acterics.healthmonitor.data.models.rest.responses;

import com.acterics.healthmonitor.data.models.Complaint;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by oleg on 20.05.17.
 */

public class ComplaintsResponse {

    @SerializedName("complaints")
    private List<Complaint> complaints;

    public List<Complaint> getComplaints() {
        return complaints;
    }

    public void setComplaints(List<Complaint> complaints) {
        this.complaints = complaints;
    }
}
