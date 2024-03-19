package org.sportApp.controller;

import org.sportApp.entities.User;
import org.sportApp.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sport_app")
public class Controller {
    private final UserService userService;

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
    public @ResponseBody ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userService.existsByLogin(user.getLogin())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User with this login already exists");
        }
        User registeredUser = userService.registerUser(user);
        if (!registeredUser.equals(user)) {
            return ResponseEntity.status(HttpStatus.VARIANT_ALSO_NEGOTIATES).body("Registered user is different from required");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }
}