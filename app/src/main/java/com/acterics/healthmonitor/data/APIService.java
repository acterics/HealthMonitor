package com.acterics.healthmonitor.data;

import com.acterics.healthmonitor.data.models.rest.requests.SignInRequest;
import com.acterics.healthmonitor.data.models.rest.requests.SignUpRequest;
import com.acterics.healthmonitor.data.models.rest.responses.AuthResponse;
import com.acterics.healthmonitor.data.models.rest.responses.BaseResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by oleg on 13.05.17.
 */

public interface APIService {


    @POST("signin")
    Call<BaseResponse<AuthResponse>> signIn(@Body SignInRequest request);

    @POST("signup")
    Call<BaseResponse<AuthResponse>> signUp(@Body SignUpRequest request);


}
