package org.sportApp.requests;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import okhttp3.*;

import org.jetbrains.annotations.NotNull;
import org.sportApp.registration.UserRegistrationDto;
import org.sportApp.training.ExerciseDto;
import org.sportApp.training.PlanDto;
import org.sportApp.training.TrainingDto;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
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
        sendAsyncPostRequest(request, future, Boolean.class, (response, result) -> {
            Log.d("backendServiceTag", "Send Request:" + (response != null) + " " + result);
            if (response != null) {
                future.complete(result);
            } else {
                Log.d("backendServiceTag", "Failed to check login existence.");
                future.completeExceptionally(new IOException("Failed to check login existence."));
            }
        });
        return future;
    }


    @NonNull
    public static CompletableFuture<UserRegistrationDto.Kind> getType(Long id) {
        CompletableFuture<UserRegistrationDto.Kind> future = new CompletableFuture<>();
        Request request = new Request.Builder().url(BASE_URL + "/getUserType/" + id).get().build();
        sendAsyncPostRequest(request, future, UserRegistrationDto.Kind.class, (response, result) -> {
            Log.d("BackendService", "Get request: " + (response != null) + " " + result);
            if (response != null) {
                future.complete(result);
            } else {
                Log.d("BackendService", "Failed to find user's type.");
                future.completeExceptionally(new IOException("Failed to find user's type."));
            }
        });
        return future;
    }

    @NonNull
    public static CompletableFuture<List<TrainingDto>> getAllTrainings(Long userId) {
        CompletableFuture<List<TrainingDto>> future = new CompletableFuture<>();
        Request request = new Request.Builder().url(BASE_URL + "/getAllTrainings/" + userId).get().build();
        Type type = new TypeToken<List<TrainingDto>>(){}.getType();
        sendAsyncPostRequest(request, future, type, (response, result) -> {
            Log.d("BackendService", "Send Request:" + (response != null) + " " + result);
            if (response != null) {
                future.complete(result);
            } else {
                Log.d("backendServiceTag", "Failed to get all trainings.");
                future.completeExceptionally(new IOException("Failed to get all trainings."));
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
    public static CompletableFuture<Long> createTraining(TrainingDto trainingDto) {
        String msg = "Saving training failed: no response from server";
        return sendRequestAndHandleResponse(BASE_URL + "/createTraining", trainingDto, Long.class, msg);
    }

    @NonNull
    public static CompletableFuture<Long> addExerciseByTrain(ExerciseDto exerciseDto, Long trainId) {
        String msg = "Saving exercise failed: no response from server";
        return sendRequestAndHandleResponse(BASE_URL + "/addExerciseByTrain/" + trainId, exerciseDto, Long.class, msg);
    }

    @NonNull
    public static CompletableFuture<Long> addExercise(ExerciseDto exerciseDto) {
        String msg = "Failed adding exercise: no response from server";
        return sendRequestAndHandleResponse(BASE_URL + "/register", exerciseDto, Long.class, msg);
    }

    @NonNull
    public static CompletableFuture<Long> addCoach(UserRegistrationDto userDto) {
        String msg = "Failed finding coach: no response from server";
        return sendRequestAndHandleResponse(BASE_URL + "/findCoach", userDto, Long.class, msg);
    }


    private static <T> void handleResponse(@NotNull Response response, CompletableFuture<T> future, Type responseClass, ResponseHandler<T> responseHandler) throws IOException {
        Log.d("BackendService", "handleResponse");
        if (!response.isSuccessful()) {
            future.completeExceptionally(new IOException("The request wasn't successful: " +
                    response.code() + " " + response.message()));
            return;
        }
        assert response.body() != null;
        String responseBody = response.body().string();
        Log.d("BackendService", "Response body in handleResponse: " + responseBody);
        T result = gson.fromJson(responseBody, responseClass);
        responseHandler.handleResponse(response, result);
        future.complete(result);
        Log.d("BackendService", "Future completed with result " + result);
    }

    @NonNull
    public static <T> CompletableFuture<T> sendRequestAndHandleResponse(String url, Object dto, Class<T> responseType, String msg) {
        CompletableFuture<T> future = new CompletableFuture<>();
        String json = gson.toJson(dto);
        Request request = createPostRequest(url, json);
        Log.d("myTag", "sendRequestAndHandleResponse1");
        try {
            Log.d("myTag", "sendRequestAndHandleResponse2");
            sendAsyncPostRequest(request, future, responseType, (response, result) -> {
                Log.d("myTag", "sendRequestAndHandleResponse3");
                Log.d("sendRequest", "sendRequest");
                Log.d("backendServiceTag", "Send Request:" + (response != null) + " " + result);
                if (response != null) {
                    future.complete(result);
                } else {
                    Log.d("backendServiceError", msg);
                    future.completeExceptionally(new IOException(msg));
                }
            });
        }
        catch (Exception e) {
            Log.d("Exception", e.getMessage());
        }
        return future;
    }


    private static <T> void sendAsyncPostRequest(@NotNull Request request, CompletableFuture<T> future, Type responseClass, ResponseHandler<T> responseHandler) {
        Log.d("BackendService", "Starting async request");
        Log.d("BackendService", "Request URL: " + request.url());
        Log.d("BackendService", "Request Headers: " + request.headers());
        Log.d("BackendService", "Request Body: " + request.body());
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                Log.d("BackendService", "Request successful, Response code: " + response.code());
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

        Log.d("BackendService", "Async request initiated");
    }
}