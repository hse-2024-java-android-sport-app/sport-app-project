package org.sportApp.requests;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.sportApp.dto.PlanDto;
import org.sportApp.dto.TrainingDto;
import org.sportApp.dto.UserDto;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class BackendService {
    static OkHttpClient client = new OkHttpClient();
    static Gson gson = new Gson();
    static String BASE_URL = "http://10.0.2.2:8080/sport_app";

    @FunctionalInterface
    interface ResponseHandler<T> {
        void handleResponse(Response response, T result) throws IOException;
    }

    @NonNull
    @Contract("_, _ -> new")
    private static OkHttpClient createAuthenticatedClient(final String username, final String password) {
        return new OkHttpClient.Builder().authenticator(
                        (route, response) -> response
                                .request()
                                .newBuilder()
                                .header("Authorization", Credentials.basic(username, password))
                                .build())
                .build();
    }

    @NonNull
    private static Request createPostRequest(String url, String json) {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(json, JSON);
        return new Request.Builder().url(url).post(body).build();
    }

    @NonNull
    public static CompletableFuture<Long> registerUser(UserDto userDto) {
        String msg = "Registration failed: no response from server";
        CompletableFuture<Long> ans = sendRequestAndHandleResponse(BASE_URL + "/register", userDto, Long.class, msg);
        client = createAuthenticatedClient(userDto.getLogin(), userDto.getPassword());
        return ans;
    }

    @NonNull
    public static CompletableFuture<Long> signInUser(UserDto userDto) {
        String msg = "Authorization failed: no response from server";
        CompletableFuture<Long> ans = sendRequestAndHandleResponse(BASE_URL + "/authorization", userDto, Long.class, msg);
        client = createAuthenticatedClient(userDto.getLogin(), userDto.getPassword());
        return ans;
    }

    @NonNull
    public static CompletableFuture<Boolean> isLoginExist(String login) {
        String url = BASE_URL + "/isLoginExist/" + login;
        return sendAsyncGetRequest(url, Boolean.class, "Failed to check login existence.");
    }

    @NonNull
    public static CompletableFuture<UserDto.Kind> getType(Long id) {
        String url = BASE_URL + "/getUserType/" + id;
        return sendAsyncGetRequest(url, UserDto.Kind.class, "Failed to find user's type.");
    }

    @NonNull
    public static CompletableFuture<UserDto> getUser(Long id) {
        String url = BASE_URL + "/getUserById/" + id;
        return sendAsyncGetRequest(url, UserDto.class, "Failed to find user's info.");
    }


    @NonNull
    public static CompletableFuture<List<String>> getNotifications(Long userId) {
        String url = BASE_URL + "/getNotifications/" + userId;
        Type type = new TypeToken<List<String>>() {
        }.getType();
        return sendAsyncGetRequest(url, type, "Failed to get " + userId + " notifications");
    }


    @NonNull
    public static CompletableFuture<Boolean> getEventIsCompleted(Long id) {
        String url = BASE_URL + "/getEventIsCompleted/" + id;
        return sendAsyncGetRequest(url, Boolean.class, "Failed to check is Completed type.");
    }

    @NonNull
    public static CompletableFuture<Boolean> getIsCoachSet(Long userId) {
        String url = BASE_URL + "/getIsCoachSet/" + userId;
        return sendAsyncGetRequest(url, Boolean.class, "Failed to check is coach set.");
    }

    @NonNull
    public static CompletableFuture<List<TrainingDto>> getAllTrainings(Long userId) {
        String url = BASE_URL + "/getAllTrainings/" + userId;
        Type type = new TypeToken<List<TrainingDto>>() {
        }.getType();
        return sendAsyncGetRequest(url, type, "Failed to get all trainings.");
    }

    @NonNull
    public static CompletableFuture<List<PlanDto>> getAllPlans(Long userId) {
        String url = BASE_URL + "/getAllPlans/" + userId;
        Type type = new TypeToken<List<PlanDto>>() {
        }.getType();
        return sendAsyncGetRequest(url, type, "Failed to get all plans.");
    }

    @NonNull
    public static CompletableFuture<List<UserDto>> getSportsmenByCoachId(Long userId) {
        String url = BASE_URL + "/getSportsmenByCoachId/" + userId;
        Type type = new TypeToken<List<UserDto>>() {
        }.getType();
        return sendAsyncGetRequest(url, type, "Failed to get coach's sportsman.");
    }

    @NonNull
    public static CompletableFuture<List<UserDto>> getFollowers(Long userId) {
        String url = BASE_URL + "/getFollowers/" + userId;
        Type type = new TypeToken<List<UserDto>>() {
        }.getType();
        return sendAsyncGetRequest(url, type, "Failed to get followers.");
    }

    @NonNull
    public static CompletableFuture<List<UserDto>> getSubscriptions(Long userId) {
        String url = BASE_URL + "/getSubscriptions/" + userId;
        Type type = new TypeToken<List<UserDto>>() {
        }.getType();
        return sendAsyncGetRequest(url, type, "Failed to get subscriptions.");
    }

    @NonNull
    public static CompletableFuture<Long> createPlan(@NonNull PlanDto planDto) {
        String msg = "Failed to add training to plan: no response from server";
        return sendRequestAndHandleResponse(BASE_URL + "/createPlan", planDto, Long.class, msg);
    }

    @NonNull
    public static CompletableFuture<Long> markEventCompleted(@NonNull Long eventId, Boolean isCompleted) {
        String msg = "Failed to mark an event as completed: no response from server";
        return sendRequestAndHandleResponse(BASE_URL + "/markEventCompleted/" + eventId, isCompleted, Long.class, msg);
    }


    @NonNull
    public static CompletableFuture<Long> createTraining(TrainingDto trainingDto) {
        String msg = "Saving training failed: no response from server";
        return sendRequestAndHandleResponse(BASE_URL + "/createTraining", trainingDto, Long.class, msg);
    }


    @NonNull
    public static CompletableFuture<Long> editCoach(Long userId, Long coachId) {
        String msg = "Failed adding coach: no response from server";
        return sendRequestAndHandleResponse(BASE_URL + "/editCoach/" + userId, coachId, Long.class, msg);
    }

    @NonNull
    public static CompletableFuture<List<UserDto>> searchCoaches(String name) {
        String url = BASE_URL + "/searchCoaches/" + name;
        Type type = new TypeToken<List<UserDto>>() {
        }.getType();
        return sendAsyncGetRequest(url, type, "Failed to find coaches by name: " + name);
    }

    @NonNull
    public static CompletableFuture<List<UserDto>> searchSportsman(String name) {
        String url = BASE_URL + "/searchSportsman/" + name;
        Type type = new TypeToken<List<UserDto>>() {
        }.getType();
        return sendAsyncGetRequest(url, type, "Failed to find sportsmen by name: " + name);
    }

    @NonNull
    public static  CompletableFuture<Void> addSubscription(Long userId, Long friendId) {
        String msg = "Failed adding coach: no response from server";
        return sendRequestAndHandleResponse(BASE_URL + "/addSubscription/" + userId, friendId, Void.class, msg);
    }


    private static <T> void handleResponse(@NotNull Response response, CompletableFuture<T> future, Type responseClass, ResponseHandler<T> responseHandler) throws IOException {
        Log.d("BackendService", "handleResponse");
        if (!response.isSuccessful()) {
            future.completeExceptionally(new IOException("The request wasn't successful: " + response.code() + " " + response.message()));
            return;
        }
        try (ResponseBody responseBody = response.body()) {
            if (responseBody == null) {
                future.completeExceptionally(new IOException("Response body is null"));
                return;
            }
            String responseBodyString = responseBody.string();
            Log.d("BackendService", "Response body in handleResponse: " + responseBodyString);
            T result = gson.fromJson(responseBodyString, responseClass);
            responseHandler.handleResponse(response, result);
            future.complete(result);
            Log.d("BackendService", "Future completed with result " + result);
        } catch (IOException e) {
            future.completeExceptionally(e);
        }
    }


    @NonNull
    public static <T> CompletableFuture<T> sendRequestAndHandleResponse(String url, Object dto, Class<T> responseType, String msg) {
        CompletableFuture<T> future = new CompletableFuture<>();
        String json = gson.toJson(dto);
        Request request = createPostRequest(url, json);
        try {
            sendAsyncRequest(request, future, responseType, (response, result) -> {
                Log.d("BackendService", "Send Request:" + (response != null) + " " + result);
                if (response != null) {
                    future.complete(result);
                } else {
                    Log.e("BackendService", msg);
                    future.completeExceptionally(new IOException(msg));
                }
            });
        } catch (Exception e) {
            Log.e("BackendService", Objects.requireNonNull(e.getMessage()));
        }
        return future;
    }


    private static <T> void sendAsyncRequest(@NotNull Request request, CompletableFuture<T> future, Type responseClass, ResponseHandler<T> responseHandler) {
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
    }


    @NonNull
    private static <T> CompletableFuture<T> sendAsyncGetRequest(String url, Class<T> responseType, String errorMsg) {
        CompletableFuture<T> future = new CompletableFuture<>();
        Request request = new Request.Builder().url(url).get().build();

        sendAsyncRequest(request, future, responseType, (response, result) -> {
            Log.d("BackendService", "Send Request:" + (response != null) + " " + result);
            if (response != null) {
                future.complete(result);
            } else {
                Log.e("BackendService", errorMsg);
                future.completeExceptionally(new IOException(errorMsg));
            }
        });

        return future;
    }

    @NonNull
    public static <T> CompletableFuture<T> sendAsyncGetRequest(String url, Type responseType, String errorMsg) {
        CompletableFuture<T> future = new CompletableFuture<>();
        Request request = new Request.Builder().url(url).get().build();

        sendAsyncRequest(request, future, responseType, (response, result) -> {
            Log.d("BackendService", "Send Request:" + (response != null) + " " + result);
            if (response != null) {
                future.complete(result);
            } else {
                Log.e("BackendService", errorMsg);
                future.completeExceptionally(new IOException(errorMsg));
            }
        });

        return future;
    }
}