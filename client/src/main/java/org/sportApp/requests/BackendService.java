package org.sportApp.requests;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import okhttp3.*;

import org.jetbrains.annotations.NotNull;
import org.sportApp.registration.UserRegistrationDto;
import org.sportApp.training.ExerciseDto;
import org.sportApp.training.PlanDto;
import org.sportApp.training.TrainingDto;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;

public class BackendService {
    static OkHttpClient client = new OkHttpClient();
    static Gson gson = new Gson();
    static String BASE_URL = "http://10.0.2.2:8080/sport_app";

    @FunctionalInterface
    interface ResponseHandler<T> {
        void handleResponse(Response response, T result) throws IOException;
    }

    @NonNull
    private static Request createPostRequest(String url, String json) {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(json, JSON);
        return new Request.Builder()
                .url(url)
                .post(body)
                .build();
    }

    @NonNull
    public static CompletableFuture<Long> registerUser(UserRegistrationDto userDto) {
        String msg = "Registration failed: no response from server";
        return sendRequestAndHandleResponse(BASE_URL + "/register", userDto, Long.class, msg);
    }

    @NonNull
    public static CompletableFuture<Long> signInUser(UserRegistrationDto userDto) {
        String msg = "Authorization failed: no response from server";
        return sendRequestAndHandleResponse(BASE_URL + "/authorization", userDto, Long.class, msg);
    }

    @NonNull
    public static CompletableFuture<Boolean> isLoginExist(String login) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        Request request = new Request.Builder().url(BASE_URL + "/isLoginExist/" + login).get().build();
        sendAsyncRequest(request, future, Boolean.class, (response, result) -> {
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


    @NonNull
    public static CompletableFuture<Long> createPlan(@NonNull PlanDto planDto) {
        String msg = "Failed to add training to plan: no response from server";
        return sendRequestAndHandleResponse(BASE_URL + "/create-plan", planDto, Long.class, msg);
    }

    @NonNull
    public static CompletableFuture<Boolean> addTrainingToPlan(long planId, TrainingDto trainingDto) {
          String msg = "Failed to add training to plan: no response from server";
          return sendRequestAndHandleResponse(BASE_URL + "/plans/" + planId + "/add-training", trainingDto, Boolean.class, msg);
    }

    @NonNull
    public static CompletableFuture<Boolean> addTraining(TrainingDto trainingDto) {
        String msg = "Saving training failed: no response from server";
        return sendRequestAndHandleResponse(BASE_URL + "/register", trainingDto, Boolean.class, msg);
    }

    @NonNull
    public static CompletableFuture<Long> addExercise(ExerciseDto exerciseDto) {
        String msg = "Failed adding exercise: no response from server";
        return sendRequestAndHandleResponse(BASE_URL + "/register", exerciseDto, Long.class, msg);
    }


    private static <T> void handleResponse(@NotNull Response response, CompletableFuture<T> future, Type responseClass, ResponseHandler<T> responseHandler) throws IOException {
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

    @NonNull
    public static <T> CompletableFuture<T> sendRequestAndHandleResponse(String url, Object dto, Class<T> responseType, String msg) {
        CompletableFuture<T> future = new CompletableFuture<>();
        String json = gson.toJson(dto);
        Log.d("myTag", json);
        Request request = createPostRequest(url, json);
        sendAsyncRequest(request, future, responseType, (response, result) -> {
            Log.d("myTag", "Send Request:" + (response != null) + " " + result);
            if (response != null) {
                future.complete(result);
            } else {
                Log.d("myTag", msg);
                future.completeExceptionally(new IOException(msg));
            }
        });
        return future;
    }


    private static <T> void sendAsyncRequest(@NotNull Request request, CompletableFuture<T> future, Type responseClass, ResponseHandler<T> responseHandler) {
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
}
