package ru.sport.app.project;

import com.google.gson.Gson;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import static org.aspectj.bridge.MessageUtil.fail;

public class UserRegistrationRequest {
    OkHttpClient client = new OkHttpClient();
    String BASE_URL = "http://localhost:8080";
    void synchronousRun() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL + "/register")
                .build();

        try (Response response = client.newCall(request).execute()) {
            Gson gson = new Gson();
            String json = response.body().string();
            System.out.println(json);
            UserRegistrationDto[] userDto = gson.fromJson(json, UserRegistrationDto[].class);
        }
    }

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
                    Gson gson = new Gson();
                    String json = responseBody.string();
                    UserRegistrationDto[] userDto = gson.fromJson(json, UserRegistrationDto[].class);
                    for (UserRegistrationDto user : userDto) {
                        System.out.println(user.getName());
                    }
                }
            }

            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println(e.getMessage());
            }
        });

    }
    void post (UserRegistrationDto userDto) {
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        String json = new Gson().toJson(userDto);
        RequestBody body = RequestBody.create(json, JSON);
        Request.Builder requestBuilder = new Request.Builder().url(BASE_URL + "/register").post(body);
        Request request = requestBuilder.build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("The request to the server was not successful: " +
                        response.code() + " " + response.message());
            }
            System.out.println(response.body().string());
        } catch (IOException e) {
            System.out.println("Connection error" + e.getMessage());
        }
    }

    public static void main(String[] args) {
        UserRegistrationRequest example = new UserRegistrationRequest();
        UserRegistrationDto user = new UserRegistrationDto("User", "login123", "123456", UserRegistrationDto.Kind.sportsman, new Date(1990, Calendar.MAY, 29));
        example.asynchronousRun();
        example.post(user);
    }
}
