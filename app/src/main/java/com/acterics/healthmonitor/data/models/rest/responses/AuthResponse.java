package com.acterics.healthmonitor.data.models.rest.responses;

import com.acterics.healthmonitor.data.models.rest.requests.SignInRequest;
import com.acterics.healthmonitor.data.models.rest.requests.SignUpRequest;
import com.google.gson.annotations.SerializedName;

/**
 * Created by oleg on 13.05.17.
 * ResponseBody for all authorization responses
 * @see com.acterics.healthmonitor.data.APIService#signIn(SignInRequest)
 * @see com.acterics.healthmonitor.data.APIService#signUp(SignUpRequest) 
 */

public class AuthResponse {
    @SerializedName("accessToken")
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
