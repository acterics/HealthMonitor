package com.acterics.healthmonitor.data;

import com.acterics.healthmonitor.data.models.Complaint;
import com.acterics.healthmonitor.data.models.UserActivity;
import com.acterics.healthmonitor.data.models.UserModel;
import com.acterics.healthmonitor.data.models.rest.requests.SignInRequest;
import com.acterics.healthmonitor.data.models.rest.requests.SignUpRequest;
import com.acterics.healthmonitor.data.models.rest.responses.AuthResponse;
import com.acterics.healthmonitor.data.models.rest.responses.BaseResponse;
import com.acterics.healthmonitor.data.models.rest.responses.ComplaintsResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by oleg on 13.05.17.
 */

public interface APIService {
    String AUTH_HEADER = "Authorization";

    @POST("signin")
    Call<BaseResponse<AuthResponse>> signIn(@Body SignInRequest request);

    @POST("signup")
    Call<BaseResponse<AuthResponse>> signUp(@Body SignUpRequest request);

    @GET("rest/complaints")
    Call<BaseResponse<ComplaintsResponse>> getComplaints(@Header(AUTH_HEADER) String token);

    @POST("rest/complaints")
    Call<BaseResponse<Complaint>> addComplaint(@Header(AUTH_HEADER) String token, @Body Complaint complaint);

    @GET("rest/complaints/{id}")
    Call<BaseResponse<Complaint>> getComplaint(@Header(AUTH_HEADER) String token, @Path("id") String id);

    @GET("rest/users/current")
    Call<BaseResponse<UserModel>> getUser(@Header(AUTH_HEADER) String token);

    @GET("rest/activity")
    Call<BaseResponse<UserActivity>> getUserActivity(@Header(AUTH_HEADER) String token, @Query("date") String date);



}
