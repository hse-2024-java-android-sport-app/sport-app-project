package org.sportApp.controller;

import org.sportApp.entities.User;
import org.sportApp.services.UserService;
import org.sportApp.dto.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.*;

@RestController
@RequestMapping("/sport_app")
public class Controller {
    private final UserService userService;
    private final ModelMapper mapper = new ModelMapper();

    @Autowired
    public Controller(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("getAllUsers")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("isLoginExist/{login}")
    public boolean isLoginExist(@PathVariable(value = "login") String login) {
        return userService.existsByLogin(login);
    }

    @PostMapping("register")
    public @ResponseBody ResponseEntity<?> registerUser(@RequestBody UserRegistrationDto userDto) {
        User user = this.mapper.map(userDto, User.class);

        if (userService.existsByLogin(user.getLogin())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with this login already exists");
        }
        User registeredUser = userService.registerUser(user);
        if (!registeredUser.equals(user)) {
            return ResponseEntity.status(HttpStatus.VARIANT_ALSO_NEGOTIATES).body("Registered user is different from required");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

    @PostMapping("authorization")
    public @ResponseBody ResponseEntity<?> authorizeUser(@RequestBody UserRegistrationDto userDto) {
        if (!userService.existsByLogin(userDto.getLogin())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User with this login doesn't exist");
        }
        long aufUserId = userService.authorizeUser(userDto.getLogin(), userDto.getPassword());
        if (aufUserId < 0) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User with this password doesn't exist");
        }
        return ResponseEntity.status(HttpStatus.OK).body(aufUserId);
    }

    @GetMapping("status")
    public @ResponseBody ResponseEntity<?> getStatus() {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}