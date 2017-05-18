package com.acterics.healthmonitor.mock;

import com.acterics.healthmonitor.data.models.IssueModel;
import com.acterics.healthmonitor.data.models.UserModel;
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
import timber.log.Timber;

/**
 * Created by oleg on 13.05.17.
 * Debug only. Class that intercept all rest requests and return mock responses.
 */

public class MockInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    private static final String MOCK_EMAIL = "admin";
    private static final String MOCK_KEY = "admin";
    private static final String MOCK_TOKEN = "qwertyuiop";
    private static final AuthResponse MOCK_AUTH_RESPONSE = new AuthResponse();
    private static final UserModel MOCK_USER = new UserModel();
    static {
        MOCK_AUTH_RESPONSE.setToken(MOCK_TOKEN);

        MOCK_USER.setFirstName("Oleg");
        MOCK_USER.setLastName("Lipskiy");
        MOCK_USER.setCountry("Ukraine");
        MOCK_USER.setCity("Kyiv");
        MOCK_USER.setHeight(180);
        MOCK_USER.setWeight(75);
        MOCK_USER.setAvatar("https://pp.userapi.com/c624730/v624730615/48c62/HgjBy4McMXA.jpg");

    }
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        RequestBody requestBody = request.body();
        HttpUrl url = chain.request().url();
        BaseResponse<?> responseBody;
        Timber.e("intercept: %s",url.encodedPath());
        Timber.e("intercept: %s", url.encodedPathSegments().get(1));
        switch (url.encodedPathSegments().get(1)) {
            case "signin":
                responseBody = processSignIn(requestBody);
                break;
            case "issues":
                responseBody = processGetIssues(requestBody);
                break;
            case "users":
                responseBody = processGetUser(chain);
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

        SignInRequest requestModel = getRequestModel(requestBody, SignInRequest.class);
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


    private BaseResponse<UserModel> processGetUser(Chain chain) {
        BaseResponse<UserModel> responseBody = new BaseResponse<>();
        responseBody.setStatus(0);
        String token = chain.request().header("Authorization");
        if (!token.equals(MOCK_TOKEN)) {
            responseBody.setStatus(403);
            responseBody.setMessage("Wrong token");
        }
        if (responseBody.getStatus() == 0) {
            responseBody.setResponse(MOCK_USER);
        }
        return responseBody;


    }

    private <T> T getRequestModel(RequestBody requestBody, Class<T> classOfModel) throws IOException {
        Buffer buffer = new Buffer();
        requestBody.writeTo(buffer);

        Charset charset = UTF8;
        MediaType contentType = requestBody.contentType();
        if (contentType != null) {
            charset = contentType.charset(UTF8);
        }

        return new Gson().fromJson(buffer.readString(charset), classOfModel);
    }

}
