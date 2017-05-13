package com.acterics.healthmonitor.data.models.rest.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by oleg on 13.05.17.
 */

public class BaseResponse<T> {

    /**
     * status code:
     *  0 - success
     *  1 - error
     */
    @SerializedName("status")
    private int status;

    @SerializedName("message")
    private String message;

    @SerializedName("response")
    private T response;


    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
