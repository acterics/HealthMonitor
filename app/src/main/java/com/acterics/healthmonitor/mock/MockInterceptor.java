package com.acterics.healthmonitor.mock;

import com.acterics.healthmonitor.data.models.IssueModel;
import com.acterics.healthmonitor.data.models.rest.requests.SignInRequest;
import com.acterics.healthmonitor.data.models.rest.responses.AuthResponse;
import com.acterics.healthmonitor.data.models.rest.responses.BaseResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Created by oleg on 13.05.17.
 */

public class MockInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    private static final String MOCK_EMAIL = "admin";
    private static final String MOCK_KEY = "admin";
    private static final AuthResponse MOCK_AUTH_RESPONSE = new AuthResponse();
    static {
        MOCK_AUTH_RESPONSE.setId(1);
        MOCK_AUTH_RESPONSE.setName("Oleg");
        MOCK_AUTH_RESPONSE.setToken("qwertyuiop");
    }
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        RequestBody requestBody = request.body();
        HttpUrl url = chain.request().url();
        BaseResponse<?> responseBody;
        switch (url.encodedPathSegments().get(0)) {
            case "signin":
                responseBody = processSignIn(requestBody);
                break;
            case "issues":
                responseBody = processGetIssues(requestBody);
                break;
            default:
                responseBody = null;
        }



        return new Response.Builder()
                .code(200)
                .request(chain.request())
                .body(ResponseBody.create(MediaType.parse("application/json"), new Gson().toJson(responseBody)))
                .protocol(Protocol.HTTP_1_0)
                .build();
    }


    private BaseResponse<AuthResponse> processSignIn(RequestBody requestBody) throws IOException {
        BaseResponse<AuthResponse> responseBody = new BaseResponse<>();
        responseBody.setStatus(0);

        Buffer buffer = new Buffer();
        requestBody.writeTo(buffer);

        Charset charset = UTF8;
        MediaType contentType = requestBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(UTF8);
        }

        SignInRequest requestModel = new Gson().fromJson(buffer.readString(charset), SignInRequest.class);
        if (!requestModel.getEmail().equalsIgnoreCase(MOCK_EMAIL)) {
            responseBody.setMessage("Wrong email");
            responseBody.setStatus(1);
        } else if (!requestModel.getPassword().equals(MOCK_KEY)) {
            responseBody.setMessage("Wrong password");
            responseBody.setStatus(1);
        }




        if (responseBody.getStatus() != 1) {
            responseBody.setResponse(MOCK_AUTH_RESPONSE);
        }

        return responseBody;
    }

    private BaseResponse<List<IssueModel>> processGetIssues(RequestBody requestBody) {
        BaseResponse<List<IssueModel>> responseBody = new BaseResponse<>();
        responseBody.setStatus(0);
        responseBody.setResponse(MockData.getIssues());
        return responseBody;
    }

}
