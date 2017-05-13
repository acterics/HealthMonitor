package com.acterics.healthmonitor.data.models.rest.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by oleg on 13.05.17.
 */

public class AuthResponse {

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private long id;

    @SerializedName("token")
    private String token;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
