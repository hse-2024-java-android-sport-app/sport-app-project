package org.sportApp.controller;

import org.sportApp.entities.*;
import org.sportApp.services.*;
import org.sportApp.dto.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.*;
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

        CompletableFuture<User> future = new CompletableFuture<>();
        if (userService.existsByLogin(user.getLogin())) {
            return future.thenApply(result -> ResponseEntity.status(HttpStatus.CONFLICT).body("User with this login already exists"));
        }

        User registeredUser = userService.registerUser(user);
        future.complete(registeredUser);
        if (!registeredUser.equals(user)) {
            return future.thenApply(result -> ResponseEntity.status(HttpStatus.VARIANT_ALSO_NEGOTIATES).body("Registered user is different from required"));
        }
        return future.thenApply(result -> ResponseEntity.status(HttpStatus.CREATED).body(registeredUser));

//        CompletableFuture<User> future = new CompletableFuture<>();
//        future.complete(taskService.getNewTasks());
//        if(yourCondition){
//            return future.thenApply(result -> new ResponseEntity<Tasks>(result, HttpStatus.STATUS_1));
//        }
//        return future.thenApply(result -> new ResponseEntity<Tasks>(result, HttpStatus.STATUS_2));
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

    @PostMapping("addTraining")
    public @ResponseBody ResponseEntity<?> authorizeUser(@RequestBody TrainingDto trainingDto) {
        Training training = this.mapper.map(trainingDto, Training.class);

        Training savedTraining = trainingService.saveTraining(training);
        if (!savedTraining.equals(training)) {
            return ResponseEntity.status(HttpStatus.VARIANT_ALSO_NEGOTIATES).body("Saved training is different from required");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTraining);
    }

    @GetMapping("getAllTrainings/{userId}")
    public @ResponseBody ResponseEntity<?> getAllTrainingsByUserId(@PathVariable(value = "userId") long userId) {
        if (!userService.existsById(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Required userId doesn't found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(trainingService.findAllByUserId(userId));
    }

    @GetMapping("getAllExercise/{trainId}")
    public @ResponseBody ResponseEntity<?> getAllExerciseByTrainId(@PathVariable(value = "trainId") long trainId) {
        Optional<Training> findTraining = trainingService.findById(trainId);
        if (findTraining.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(findTraining.get().getExercises());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Required training doesn't found");
    }

    @GetMapping("status")
    public @ResponseBody ResponseEntity<?> getStatus() {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}