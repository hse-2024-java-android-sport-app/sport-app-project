package org.sportApp.requests;
import com.google.gson.Gson;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import org.sportApp.registration.*;

public class BackendService {
    OkHttpClient client = new OkHttpClient();
    Gson gson = new Gson();
    String BASE_URL = "http://localhost:8080";

    void asynchronousRun() {
        Request request = new Request.Builder()
                .url(BASE_URL + "/register")
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            public void onResponse(@NotNull Call call, @NotNull Response response)
                    throws IOException {
                handleResponse(response, null, null);
            }
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(e.getMessage());
            }
        });
    }

    public CompletableFuture<RegistrationResultDto> registerUser(UserRegistrationDto userDto) {
        CompletableFuture<RegistrationResultDto> future = new CompletableFuture<>();
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        String json = new Gson().toJson(userDto);
        RequestBody body = RequestBody.create(json, JSON);
        Request.Builder requestBuilder = new Request.Builder().url(BASE_URL + "/register").post(body);
        performRequest(requestBuilder, future, RegistrationResultDto.class);
        return future;
    }

    public CompletableFuture<Boolean> checkLogin(String login) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        String json = "{\"login\": \"" + login + "\"}";
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(BASE_URL + "/check-login")
                .post(body)
                .build();
        performRequest(new Request.Builder().url(BASE_URL + "/check-login").post(body), future, Boolean.class);
        return future;
    }

    private <T> void performRequest(Request.Builder requestBuilder, CompletableFuture<T> future, Class<T> responseType) {
        Request request = requestBuilder.build();
        client.newCall(request).enqueue(new Callback() {
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                handleResponse(response, future, responseType);
            }

            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                future.completeExceptionally(e);
            }
        });
    }

    private <T> void handleResponse(Response response, CompletableFuture<T> future, Class<T> responseType) throws IOException {
        if (!response.isSuccessful()) {
            if (future != null) {
                future.completeExceptionally(new IOException("The request to the server was not successful: " +
                        response.code() + " " + response.message()));
            }
            return;
        }
        assert response.body() != null;
        String responseBodyString = response.body().string();
        if (future != null) {
            T result = gson.fromJson(responseBodyString, responseType);
            future.complete(result);
        }
    }
}
