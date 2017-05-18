package com.acterics.healthmonitor.data;

import com.acterics.healthmonitor.data.models.IssueModel;
import com.acterics.healthmonitor.data.models.UserModel;
import com.acterics.healthmonitor.data.models.rest.requests.SignInRequest;
import com.acterics.healthmonitor.data.models.rest.requests.SignUpRequest;
import com.acterics.healthmonitor.data.models.rest.responses.AuthResponse;
import com.acterics.healthmonitor.data.models.rest.responses.BaseResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/**
 * Created by oleg on 13.05.17.
 */

public interface APIService {
    String AUTH_HEADER = "Authorization";

    @POST("signin")
    Call<BaseResponse<AuthResponse>> signIn(@Body SignInRequest request);

    @POST("signup")
    Call<BaseResponse<AuthResponse>> signUp(@Body SignUpRequest request);

    @POST("issues")
    Call<BaseResponse<List<IssueModel>>> getIssues(@Header(AUTH_HEADER) String token);

    @GET("users/current")
    Call<BaseResponse<UserModel>> getUser(@Header(AUTH_HEADER) String token);



}
