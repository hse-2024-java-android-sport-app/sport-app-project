package org.sportApp.registration;

import com.google.gson.Gson;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class UserRegistrationService {
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
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) {
                        throw new IOException("The request to the server was not successful: " +
                                response.code() + " " + response.message());
                    }
                    String json = responseBody.string();
                    UserRegistrationDto[] userDto = gson.fromJson(json, UserRegistrationDto[].class);
                    for (UserRegistrationDto user : userDto) {
                        System.out.println(user.getLogin()); // for testing
                    }
                }
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
        Request request = requestBuilder.build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("The request to the server was not successful: " +
                            response.code() + " " + response.message());
                }
                RegistrationResultDto registrationResultDto = new RegistrationResultDto();
                future.complete(registrationResultDto);
            }

            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                future.completeExceptionally(e);
            }
        });
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
        client.newCall(request).enqueue(new Callback() {
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (!response.isSuccessful()) {
                    future.completeExceptionally(new IOException("The request to the server was not successful: " +
                            response.code() + " " + response.message()));
                    return;
                }
                String responseBodyString = response.body().string();
                boolean exists = gson.fromJson(responseBodyString, Boolean.class);
                future.complete(exists);
            }
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }
}