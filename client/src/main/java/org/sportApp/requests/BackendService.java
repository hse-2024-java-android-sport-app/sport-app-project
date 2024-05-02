package org.sportApp.requests;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import okhttp3.*;

import org.jetbrains.annotations.NotNull;
import org.sportApp.registration.UserRegistrationDto;
import org.sportApp.training.PlanDto;
import org.sportApp.training.TrainingDto;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BackendService {
    OkHttpClient client = new OkHttpClient();
    Gson gson = new Gson();
    String BASE_URL = "http://10.0.2.2:8080/sport_app";

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

    public CompletableFuture<Long> registerUser(UserRegistrationDto userDto) {
        CompletableFuture<Long> future = new CompletableFuture<>();
        String json = gson.toJson(userDto);
        Log.d("myTag", json);
        Request request = createPostRequest(BASE_URL + "/register", json);
        sendRequest(request, future, Long.class, (response, result) -> {
            Log.d("myTag", "Send Request:" + (response != null) + " " + result);
            if (response != null) {
                future.complete(result);
            } else {
                Log.d("myTag", "Registration failed: no response from server");
                future.completeExceptionally(new IOException("Registration failed: no response from server"));
            }
        });
        return future;
    }

    public CompletableFuture<Long> getUserProfile(String login) {
        CompletableFuture<Long> future = new CompletableFuture<>();
        String url = BASE_URL + "/getUser" + login;
        Request request = new Request.Builder().url(url).get().build();
        sendRequest(request, future, Long.class, (response, result) -> {
            Log.d("myTag", "Send Request:" + (response != null) + " " + result);
            if (response != null) {
                future.complete(result);
            } else {
                Log.d("myTag", "Failed to get user profile.");
                future.completeExceptionally(new IOException("Failed to get user profile."));
            }
        });

        return future;
    }


    public CompletableFuture<Boolean> isLoginExist(String login) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        String json = "{\"login\": \"" + login + "\"}";
        Request request = createPostRequest(BASE_URL + "/isLoginExist", json);
        sendRequest(request, future, Boolean.class, (response, result) -> {
            Log.d("myTag", "Send Request:" + (response != null) + " " + result);
            if (response != null) {
                future.complete(result);
            } else {
                Log.d("myTag", "Failed to check login existence.");
                future.completeExceptionally(new IOException("Failed to check login existence."));
            }
        });
        return future;
    }

    public CompletableFuture<List<Long>> getAllUsers() {
        CompletableFuture<List<Long>> future = new CompletableFuture<>();
        Request request = new Request.Builder().url(BASE_URL + "/getAllUsers").get().build();
        sendRequest(request, future, new TypeToken<List<Long>>() {}.getType(), (response, result) -> {
            Log.d("myTag", "Send Request:" + (response != null) + " " + result);
            if (response != null) {
                future.complete(result);
            } else {
                Log.d("myTag", "Failed to get all users.");
                future.completeExceptionally(new IOException("Failed to get all users."));
            }
        });

        return future;
    }

    public void createPlan(@NonNull PlanDto planDto) {
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

    public CompletableFuture<Boolean> addTrainingToPlan(long planId, TrainingDto trainingDto) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        String json = gson.toJson(trainingDto);
        Request request = createPostRequest(BASE_URL + "/plans/" + planId + "/add-training", json);
        sendRequest(request, future, Boolean.class, (response, result) -> {
            if (response != null) {
                if (result) {
                    future.complete(true);
                } else {
                    future.completeExceptionally(new IOException("Failed to add training to plan"));
                }
            } else {
                future.completeExceptionally(new IOException("No response from server"));
            }
        });
        return future;
    }

    public CompletableFuture<Boolean> addTraining(TrainingDto trainingDto) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        String json = gson.toJson(trainingDto);
        Request request = createPostRequest(BASE_URL + "/add-training", json);
        sendRequest(request, future, Boolean.class, (response, result) -> {
            if (response != null) {
                if (result) {
                    future.complete(true);
                } else {
                    future.completeExceptionally(new IOException("Failed to save training"));
                }
            } else {
                future.completeExceptionally(new IOException("No response from server"));
            }
        });
        return future;
    }

    private <T> void sendRequest(@NotNull Request request, CompletableFuture<T> future, Type responseClass, ResponseHandler<T> responseHandler) {
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

    private <T> void handleResponse(@NotNull Response response, CompletableFuture<T> future, Type responseClass, ResponseHandler<T> responseHandler) throws IOException {
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
