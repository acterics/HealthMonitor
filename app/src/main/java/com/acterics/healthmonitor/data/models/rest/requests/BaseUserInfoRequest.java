package com.acterics.healthmonitor.data.models.rest.requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by oleg on 14.05.17.
 */

public class BaseUserInfoRequest {
    @SerializedName("id")
    private long id;

    @SerializedName("token")
    private String token;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
