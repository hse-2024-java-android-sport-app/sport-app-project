package org.sportApp.controller;

import org.sportApp.entities.*;
import org.sportApp.services.*;
import org.sportApp.dto.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.*;

import java.util.ArrayList;
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

    @GetMapping("getUserType/{userId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> getUserType(@PathVariable(value = "userId") long userId) {
        return userService.getUserType(userId)
                .<CompletableFuture<ResponseEntity<?>>>map(userType -> CompletableFuture.supplyAsync(
                        () -> ResponseEntity.status(HttpStatus.OK).body(userType)))
                .orElseGet(() -> CompletableFuture.supplyAsync(
                        () -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Required userId doesn't found")));
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

    @PostMapping("addExercise/{trainId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> addExercise(@PathVariable(value = "trainId") long trainId, @RequestBody ExerciseDto exerciseDto) {
        Exercise exercise = this.mapper.map(exerciseDto, Exercise.class);

        Optional<Exercise> addedExercise = trainingService.addExercise(trainId, exercise);
        CompletableFuture<Optional<Exercise>> future = CompletableFuture.supplyAsync(() -> addedExercise);
        if (addedExercise.isEmpty()) {
            return future.thenApply(result -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Training with this ID doesn't exist"));
        }
        if (!addedExercise.get().equals(exercise)) {
            return future.thenApply(result -> ResponseEntity.status(HttpStatus.VARIANT_ALSO_NEGOTIATES).body("Saved exercise is different from required"));
        }
        return future.thenApply(result -> ResponseEntity.status(HttpStatus.CREATED).body(addedExercise.get().getId()));
    }

    @GetMapping("getAllTrainings/{userId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> getAllTrainingsByUserId(@PathVariable(value = "userId") long userId) {
        if (userService.notExistsById(userId)) {
            return CompletableFuture.supplyAsync(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Required userId doesn't found"));
        }
        List<Training> trainingList = new ArrayList<>();
        trainingService.findAllByUserId(userId).forEach(trainingList::add);
        return CompletableFuture.supplyAsync(() -> ResponseEntity.status(HttpStatus.OK).body(
                trainingList.stream()
                        .map(training -> mapper.map(training, TrainingDto.class))
                        .toList()));
    }

    @GetMapping("getAllExercise/{trainId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> getAllExerciseByTrainId(@PathVariable(value = "trainId") long trainId) {
        return trainingService.findById(trainId)
                .<CompletableFuture<ResponseEntity<?>>>map(training -> CompletableFuture.supplyAsync(
                        () -> ResponseEntity.status(HttpStatus.OK).body(
                                training.getExercises().stream()
                                .map(exr -> this.mapper.map(exr, ExerciseDto.class))
                                .toList())))
                .orElseGet(() -> CompletableFuture.supplyAsync(
                        () -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Required training doesn't found")));
    }

    @GetMapping("status")
    public @ResponseBody ResponseEntity<?> getStatus() {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}