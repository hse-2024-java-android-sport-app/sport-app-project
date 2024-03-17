package org.sportApp.controller;

import org.sportApp.repo.UserRepository;
import org.sportApp.entities.User;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path="/sport_app")
public class Controller {
    private final UserRepository userRepository;
    public Controller(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @RequestMapping (value = "/{id}")
    public User findById(@PathVariable("id") long id) {
        return userRepository.findById(id).orElse(null);
    }

    @PostMapping(path="/create") // Map ONLY POST Requests
    public @ResponseBody String createUser (@RequestBody User user) {
        userRepository.save(user);
        return "Saved";
    }
}