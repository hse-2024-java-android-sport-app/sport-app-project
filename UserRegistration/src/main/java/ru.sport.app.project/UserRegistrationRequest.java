package ru.sport.app.project;

import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;

public class UserRegistrationRequest {
    OkHttpClient client = new OkHttpClient();

    String BASE_URL = "http://localhost:8080";


    void run() throws IOException {
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

    public static void main(String[] args) throws IOException {
        UserRegistrationRequest example = new UserRegistrationRequest();
        example.run();
    }
}
