package com.acterics.healthmonitor.data.models.rest.requests;

import com.google.gson.annotations.SerializedName;

/**
 * Created by oleg on 13.05.17.
 * RequestBody for {@link com.acterics.healthmonitor.data.APIService#signIn(SignInRequest) request}
 */

public class SignInRequest {

    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
