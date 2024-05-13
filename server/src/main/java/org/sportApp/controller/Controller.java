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
    private final EventService eventService;
    private final PlanService planService;
    private final ModelMapper mapper = new ModelMapper();

    @Autowired
    public Controller(UserService userService, TrainingService trainingService, EventService eventService, PlanService planService) {
        this.userService = userService;
        this.trainingService = trainingService;
        this.eventService = eventService;
        this.planService = planService;
    }

    @GetMapping("http://10.0.2.2:8080/sportApp/getAllUsers")
    public @ResponseBody Iterable<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("status")
    public @ResponseBody ResponseEntity<?> getStatus() {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }



    // USER
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

    @GetMapping("getSportsmenByCoachId/{coachId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> getSportsmenByCoachId(@PathVariable(value = "coachId") long coachId) {
        List<User> sportsmenList = new ArrayList<>();
        userService.getAllSportsmenByCoachId(coachId).forEach(sportsmenList::add);
        return CompletableFuture.supplyAsync(() -> ResponseEntity.status(HttpStatus.OK).body(
                sportsmenList.stream()
                        .map(user -> mapper.map(user, UserRegistrationDto.class))
                        .toList()
        ));
    }

    /** TODO
     *  isUserExist or isCoachExist
     *  addCoach
     *
     */


    // TRAINING
    @PostMapping("createTraining")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> createTraining(@RequestBody TrainingDto trainingDto) {
        Training training = this.mapper.map(trainingDto, Training.class);

        Training savedTraining = trainingService.saveTraining(training);
        CompletableFuture<Training> future = CompletableFuture.supplyAsync(() -> savedTraining);
        if (!savedTraining.equals(training)) {
            return future.thenApply(result -> ResponseEntity.status(HttpStatus.VARIANT_ALSO_NEGOTIATES).body("Saved training is different from required"));
        }
        return future.thenApply(result -> ResponseEntity.status(HttpStatus.CREATED).body(savedTraining.getTrainId()));
    }

    @PostMapping("addExerciseByTrain/{trainId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> addExerciseByTrain(@PathVariable(value = "trainId") long trainId, @RequestBody ExerciseDto exerciseDto) {
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

    @GetMapping("getAllExerciseByTrain/{trainId}")
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



    // EVENT
    @PostMapping("addExerciseByEvent/{eventId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> addExerciseByEvent(@PathVariable(value = "eventId") long eventId, @RequestBody ExerciseDto exerciseDto) {
        Exercise exercise = this.mapper.map(exerciseDto, Exercise.class);

        Optional<Exercise> addedExercise = eventService.addExercise(eventId, exercise);
        CompletableFuture<Optional<Exercise>> future = CompletableFuture.supplyAsync(() -> addedExercise);
        if (addedExercise.isEmpty()) {
            return future.thenApply(result -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event with this ID doesn't exist"));
        }
        if (!addedExercise.get().equals(exercise)) {
            return future.thenApply(result -> ResponseEntity.status(HttpStatus.VARIANT_ALSO_NEGOTIATES).body("Saved exercise is different from required"));
        }
        return future.thenApply(result -> ResponseEntity.status(HttpStatus.CREATED).body(addedExercise.get().getId()));
    }

//    @PostMapping("setCompleted/{eventId}")
//    public @ResponseBody CompletableFuture<ResponseEntity<?>> setCompleted(@PathVariable(value = "eventId") long eventId) {
//        return ;
//    }

    @GetMapping("getTrainingByEvent/{eventId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> getTrainingByEventId(@PathVariable(value = "eventId") long eventId) {
        return eventService.findTrainingById(eventId)
                .<CompletableFuture<ResponseEntity<?>>>map(training -> CompletableFuture.supplyAsync(
                        () -> ResponseEntity.status(HttpStatus.OK).body(mapper.map(training, TrainingDto.class))))
                .orElseGet(() -> CompletableFuture.supplyAsync(
                        () -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Required training doesn't found")));
    }

    @GetMapping("getAllExerciseByEvent/{eventId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> getAllExerciseByEventId(@PathVariable(value = "eventId") long eventId) {
        return eventService.findTrainingById(eventId)
                .<CompletableFuture<ResponseEntity<?>>>map(training -> CompletableFuture.supplyAsync(
                        () -> ResponseEntity.status(HttpStatus.OK).body(
                                training.getExercises().stream()
                                        .map(exr -> this.mapper.map(exr, ExerciseDto.class))
                                        .toList())))
                .orElseGet(() -> CompletableFuture.supplyAsync(
                        () -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Required training doesn't found")));
    }

    @GetMapping("getEventIsCompleted/{eventId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> getEventIsCompleted(@PathVariable(value = "eventId") long eventId) {
        return eventService.isCompeted(eventId)
                .<CompletableFuture<ResponseEntity<?>>>map(isCompleted -> CompletableFuture.supplyAsync(
                        () -> ResponseEntity.status(HttpStatus.OK).body(isCompleted)))
                .orElseGet(() -> CompletableFuture.supplyAsync(
                        () -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event with this ID doesn't exist")));
    }


    // PLAN
    @PostMapping("createPlan")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> createPlan(@RequestBody PlanDto planDto) {
        Plan plan = this.mapper.map(planDto, Plan.class);

        Plan savedPlan = planService.savePlan(plan);
        CompletableFuture<Plan> future = CompletableFuture.supplyAsync(() -> savedPlan);
        if (!savedPlan.equals(plan)) {
            return future.thenApply(result -> ResponseEntity.status(HttpStatus.VARIANT_ALSO_NEGOTIATES).body("Saved plan is different from required"));
        }
        return future.thenApply(result -> ResponseEntity.status(HttpStatus.CREATED).body(savedPlan.getPlanId()));
    }

    @PostMapping("addEvent/{planId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> addEventByPlanId(@PathVariable(value = "planId") long planId, @RequestBody TrainingEventDto eventDto) {
        TrainingEvent event = this.mapper.map(eventDto, TrainingEvent.class);

        Optional<TrainingEvent> addedEvent = planService.addEvent(planId, event);
        CompletableFuture<Optional<TrainingEvent>> future = CompletableFuture.supplyAsync(() -> addedEvent);
        if (addedEvent.isEmpty()) {
            return future.thenApply(result -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plan with this ID doesn't exist"));
        }
        if (!addedEvent.get().equals(event)) {
            return future.thenApply(result -> ResponseEntity.status(HttpStatus.VARIANT_ALSO_NEGOTIATES).body("Saved event is different from required"));
        }
        return future.thenApply(result -> ResponseEntity.status(HttpStatus.CREATED).body(addedEvent.get().getEventId()));
    }

    @GetMapping("getAllPlans/{sportsmanId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> getAllPlansBySportsmanId(@PathVariable(value = "sportsmanId") long sportsmanId) {
        Iterable<Plan> planDtos = planService.findAllPlansBySportsmanId(sportsmanId);
//        planDtos.forEach(plan -> this.mapper.map(plan, PlanDto.class));
        //TODO
        List<Long> ids = new ArrayList<>();
        planDtos.forEach(pl -> ids.add(pl.getPlanId()));
        return CompletableFuture.supplyAsync(() -> ResponseEntity.status(HttpStatus.OK).body(ids));
    }

    @GetMapping("getPlan/{planId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> getPlanByPlanId(@PathVariable(value = "planId") long planId) {
        return planService.findPlanByPlanId(planId)
                .<CompletableFuture<ResponseEntity<?>>>map(plan -> CompletableFuture.supplyAsync(
                        () -> ResponseEntity.status(HttpStatus.OK).body(mapper.map(plan, PlanDto.class))))
                .orElseGet(() -> CompletableFuture.supplyAsync(
                        () -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plan with this ID doesn't exist")));

    }

    @GetMapping("getAllNotCompletedPlans/{sportsmanId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> getAllNotCompletedPlans(@PathVariable(value = "sportsmanId") long sportsmanId) {
        List<Plan> planEnt = planService.findAllNotCompletedPlansBySportsmanId(sportsmanId);
        planEnt.forEach(plan -> this.mapper.map(plan, PlanDto.class));
        return CompletableFuture.supplyAsync(() -> ResponseEntity.status(HttpStatus.OK).body(planEnt));
    }

    @GetMapping("getPlanIsCompleted/{planId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> getPlanIsCompleted(@PathVariable(value = "planId") long planId) {
        return planService.isCompeted(planId)
                .<CompletableFuture<ResponseEntity<?>>>map(isCompleted -> CompletableFuture.supplyAsync(
                        () -> ResponseEntity.status(HttpStatus.OK).body(isCompleted)))
                .orElseGet(() -> CompletableFuture.supplyAsync(
                        () -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plan with this ID doesn't exist")));
    }
}