package ru.sport.app.project;

import com.google.gson.Gson;
import okhttp3.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class RegistrationTest {

    OkHttpClient client = new OkHttpClient();
    String BASE_URL = "http://localhost:8080/register";

    @Test
    public void whenGetRequest_thenCorrect() throws IOException {
        Request request = new Request.Builder()
                .url(BASE_URL)
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();

        Assertions.assertEquals(response.code(), 200);
    }

    @Test
    public void whenPostJson_thenCorrect() throws IOException {
        UserRegistrationDto userDto = new UserRegistrationDto("Diana", "mediana105", "12345", UserRegistrationDto.Kind.sportsman, new Date(2002, Calendar.MAY, 10));
        String json = new Gson().toJson(userDto);

        RequestBody body = RequestBody.create(json,
                MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url(BASE_URL)
                .post(body)
                .build();

        Call call = client.newCall(request);
        Response response = call.execute();

        Assertions.assertEquals(response.code(), 200);
    }
}
