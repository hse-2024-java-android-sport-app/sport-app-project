package ru.sport.app.project.controller;

import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.sport.app.project.UserRegistrationDto;

import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping("/register")
public class UserRegistrationController {
    private final List<UserRegistrationDto> users = new ArrayList<>();

    @GetMapping
    public List<UserRegistrationDto> getAllUsers() {
        return users;
    }

    @PostMapping
    public String createUser(@RequestBody String user) {
        Gson gson = new Gson();
        UserRegistrationDto userDto = gson.fromJson(user, UserRegistrationDto.class);
        users.add(userDto);
        return "User created\n";
    }
}