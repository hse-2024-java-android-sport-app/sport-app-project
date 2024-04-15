package org.sportApp.requests;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.sportApp.registration.RegistrationResultDto;
import org.sportApp.registration.UserRegistrationDto;
import org.sportApp.training.PlanDto;
import org.sportApp.training.PlanResultDto;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class BackendService {
    OkHttpClient client = new OkHttpClient();
    Gson gson = new Gson();
    String BASE_URL = "http://localhost:8080";

    @FunctionalInterface
    interface ResponseHandler<T> {
        void handleResponse(Response response, T result) throws IOException;
    }

    private Request createPostRequest(String url, String json) {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(json, JSON);
        return new Request.Builder()
                .url(url)
                .post(body)
                .build();
    }

    public CompletableFuture<RegistrationResultDto> registerUser(UserRegistrationDto userDto) {
        CompletableFuture<RegistrationResultDto> future = new CompletableFuture<>();
        String json = gson.toJson(userDto);
        Request request = createPostRequest(BASE_URL + "/register", json);
        sendRequest(request, future, RegistrationResultDto.class, (response, result) -> {
            if (response != null) {
                if (result.isSuccessful()) {
                    long userId = result.getUserId();
                    result.setUserId(userId);
                    future.complete(result);
                } else {
                    future.completeExceptionally(new IOException("Registration failed"));
                }
            } else {
                System.out.println("Registration failed: no response from server");
                future.completeExceptionally(new IOException("Registration failed: no response from server"));
            }
        });
        return future;
    }

    public CompletableFuture<Boolean> checkLogin(String login) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        String json = "{\"login\": \"" + login + "\"}";
        Request request = createPostRequest(BASE_URL + "/check-login", json);
        sendRequest(request, future, Boolean.class, (response, result) -> {});
        return future;
    }

    public void createPlan(PlanDto planDto) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userId", planDto.getUserId());
        String json = jsonObject.toString();
        Request request = createPostRequest(BASE_URL + "/create-plan", json);
        CompletableFuture<PlanResultDto> future = new CompletableFuture<>();
        sendRequest(request, future, PlanResultDto.class, (response, result) -> {
            if (response != null) {
                if (result.isSuccessful()) {
                    long userId = result.getUserId();
                    result.setUserId(userId);
                    future.complete(result);
                } else {
                    future.completeExceptionally(new IOException("Plan creating failed"));
                }
            } else {
                future.completeExceptionally(new IOException("Plan creating failed: no response from server"));
            }
        });
    }

    private <T> void sendRequest(@NotNull Request request, CompletableFuture<T> future, Class<T> responseClass, ResponseHandler<T> responseHandler) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try {
                    handleResponse(response, future, responseClass, responseHandler);
                } catch (IOException e) {
                    future.completeExceptionally(e);
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                future.completeExceptionally(e);
            }
        });
    }

    private <T> void handleResponse(@NotNull Response response, CompletableFuture<T> future, Class<T> responseClass, ResponseHandler<T> responseHandler) throws IOException {
        if (!response.isSuccessful()) {
            future.completeExceptionally(new IOException("The request wasn't successful: " +
                    response.code() + " " + response.message()));
            return;
        }
        assert response.body() != null;
        String responseBody = response.body().string();
        T result = gson.fromJson(responseBody, responseClass);
        responseHandler.handleResponse(response, result);
        future.complete(result);
    }
}
