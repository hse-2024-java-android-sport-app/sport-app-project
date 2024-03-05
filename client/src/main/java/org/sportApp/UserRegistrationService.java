package org.sportApp;

import com.google.gson.Gson;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
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
                        System.out.println(user.getName()); // for testing
                    }
                }
            }
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(e.getMessage());
            }
        });

    }

    CompletableFuture<RegistrationResultDto> registerUser(UserRegistrationDto userDto) {
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

    public static void main(String[] args) {
        UserRegistrationService example = new UserRegistrationService();
        UserRegistrationDto user = new UserRegistrationDto("User", "login123", "123456", UserRegistrationDto.Kind.sportsman, new Date(1990, Calendar.MAY, 29));
        example.asynchronousRun();
        example.registerUser(user);
    }
}
