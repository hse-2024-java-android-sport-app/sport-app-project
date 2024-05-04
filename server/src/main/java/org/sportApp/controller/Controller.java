package org.sportApp.controller;

import org.sportApp.entities.*;
import org.sportApp.services.*;
import org.sportApp.dto.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/sport_app")
public class Controller {
    private final UserService userService;
    private final TrainingService trainingService;
    private final ModelMapper mapper = new ModelMapper();

    @Autowired
    public Controller(UserService userService, TrainingService trainingService) {
        this.userService = userService;
        this.trainingService = trainingService;
    }

    @GetMapping("http://10.0.2.2:8080/sportApp/getAllUsers")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("isLoginExist/{login}")
    public boolean isLoginExist(@PathVariable(value = "login") String login) {
        return userService.existsByLogin(login);
    }

    @PostMapping("register")
    public @ResponseBody
    CompletableFuture<ResponseEntity<?>> registerUser(@RequestBody UserRegistrationDto userDto) {
        User user = this.mapper.map(userDto, User.class);
        if (userService.existsByLogin(user.getLogin())) {
            return CompletableFuture.supplyAsync(() -> ResponseEntity.status(HttpStatus.CONFLICT).body("User with this login already exists"));
        }

        User registeredUser = userService.registerUser(user);
        CompletableFuture<User> future = CompletableFuture.supplyAsync(() -> registeredUser);
        if (!registeredUser.equals(user)) {
            return future.thenApply(result -> ResponseEntity.status(HttpStatus.VARIANT_ALSO_NEGOTIATES).body("Registered user is different from required"));
        }
        return future.thenApply(result -> ResponseEntity.status(HttpStatus.CREATED).body(registeredUser.getId()));
    }

    @PostMapping("authorization")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> authorizeUser(@RequestBody UserRegistrationDto userDto) {

        if (!userService.existsByLogin(userDto.getLogin())) {
            return CompletableFuture.supplyAsync(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User with this login doesn't exist"));
        }

        long aufUserId = userService.authorizeUser(userDto.getLogin(), userDto.getPassword());
        CompletableFuture<Long> future = CompletableFuture.supplyAsync(() -> aufUserId);
        if (aufUserId < 0) {
            return future.thenApply(result -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User with this password doesn't exist"));
        }
        return future.thenApply(result -> ResponseEntity.status(HttpStatus.OK).body(aufUserId));
    }

    @PostMapping("addTraining")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> addTraining(@RequestBody TrainingDto trainingDto) {
        Training training = this.mapper.map(trainingDto, Training.class);

        Training savedTraining = trainingService.saveTraining(training);
        CompletableFuture<Training> future = CompletableFuture.supplyAsync(() -> savedTraining);
        if (!savedTraining.equals(training)) {
            return future.thenApply(result -> ResponseEntity.status(HttpStatus.VARIANT_ALSO_NEGOTIATES).body("Saved training is different from required"));
        }
        return future.thenApply(result -> ResponseEntity.status(HttpStatus.CREATED).body(savedTraining.getTrainId()));
    }

    @GetMapping("getAllTrainings/{userId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> getAllTrainingsByUserId(@PathVariable(value = "userId") long userId) {
        if (!userService.existsById(userId)) {
            return CompletableFuture.supplyAsync(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Required userId doesn't found"));
        }
        return CompletableFuture.supplyAsync(() -> ResponseEntity.status(HttpStatus.OK).body(trainingService.findAllByUserId(userId)));
    }

    @GetMapping("getAllExercise/{trainId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> getAllExerciseByTrainId(@PathVariable(value = "trainId") long trainId) {
        Optional<Training> findTraining = trainingService.findById(trainId);
        CompletableFuture<List<Exercise>> future = new CompletableFuture<>();
        return findTraining
                .<CompletableFuture<ResponseEntity<?>>>map(training ->
                        CompletableFuture.supplyAsync(
                                () -> ResponseEntity.status(HttpStatus.OK).body(training.getExercises())))
                .orElseGet(() ->
                        CompletableFuture.supplyAsync(
                                () -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Required training doesn't found")));
    }

    @GetMapping("status")
    public @ResponseBody ResponseEntity<?> getStatus() {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}