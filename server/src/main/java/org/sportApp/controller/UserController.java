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
import java.util.function.Function;

/**
 * Controller for user registration, authorization
 * Search, add, edit coach
 * Get sportsmen for coach
 * Search, add, get subscribers and followers
 * Get notifications
 */
@RestController
@RequestMapping("/sport_app")
public class UserController {
    private final UserService userService;
    private final TrainingService trainingService;
    private final EventService eventService;
    private final PlanService planService;
    private final NotificationService notifService;
    private final ModelMapper mapper = new ModelMapper();

    @Autowired
    public UserController(UserService userService, TrainingService trainingService, EventService eventService, PlanService planService, NotificationService notifService) {
        this.userService = userService;
        this.trainingService = trainingService;
        this.eventService = eventService;
        this.planService = planService;
        this.notifService = notifService;
    }

    //todo mapper user -> null for password

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
    public @ResponseBody CompletableFuture<ResponseEntity<?>> isLoginExist(@PathVariable(value = "login") String login) {
        return CompletableFuture.supplyAsync(() -> ResponseEntity.status(HttpStatus.OK).body(userService.existsByLogin(login)));
    }

    @PostMapping("register")
    public @ResponseBody
    CompletableFuture<ResponseEntity<?>> registerUser(@RequestBody UserDto userDto) {
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
    public @ResponseBody CompletableFuture<ResponseEntity<?>> authorizeUser(@RequestBody UserDto userDto) {
        if (!userService.existsByLogin(userDto.getLogin())) {
            return CompletableFuture.supplyAsync(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User with this login doesn't exist"));
        }
        return userService.authorizeUser(userDto.getLogin(), userDto.getPassword())
                .<CompletableFuture<ResponseEntity<?>>>map(user -> CompletableFuture.supplyAsync(
                        () -> ResponseEntity.status(HttpStatus.OK).body(user.getId())))
                .orElseGet(() -> CompletableFuture.supplyAsync(
                        () -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User with this password doesn't exist")));
    }

    @GetMapping("getUserType/{userId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> getUserType(@PathVariable(value = "userId") long userId) {
        return convertOptionalToFuture(userService.getUserType(userId), UserDto.Kind.class, "Required userId doesn't found");
    }

    @GetMapping("getUserById/{userId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> getUser(@PathVariable(value = "userId") long userId) {
        return convertOptionalToFuture(userService.getUser(userId), UserDto.class, "Required userId doesn't found");
    }

    @GetMapping("getSportsmenByCoachId/{coachId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> getSportsmenByCoachId(@PathVariable(value = "coachId") long coachId) {
        return checkIdExistAndConvertListToFuture(
                userService.getUserAndCheckType(coachId, User.Kind.coach),
                coach -> userService.getAllSportsmenByCoachId(coach.getId()),
                String.class,
                "Required coach doesn't found");
    }

    @GetMapping("getIsCoachSet/{sportsmanId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> getIsCoachSet(@PathVariable(value = "sportsmanId") long sportsmanId) {
        return convertOptionalToFuture(
                userService.getUserAndCheckType(sportsmanId, User.Kind.sportsman)
                        .map(user -> user.getCoach() == null),
                Boolean.class,
                "Required sportsmanId doesn't found");
    }

    @GetMapping("searchCoaches/{searchString}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> searchCoaches(@PathVariable(value = "searchString") String searchString) {
        return CompletableFuture.supplyAsync(() -> ResponseEntity.status(HttpStatus.OK).body(
                userService.searchUser(searchString, User.Kind.coach).stream()
                        .map(user -> mapper.map(user, UserDto.class))
                        .toList()
        ));
    }

    @PostMapping("editCoach/{sportsmanId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> editCoach(@PathVariable(value = "sportsmanId") long sportsmanId, @RequestBody long coachId) {
        return userService.editCoach(sportsmanId, coachId)
                .<CompletableFuture<ResponseEntity<?>>>map(userId -> CompletableFuture.supplyAsync(
                        () -> ResponseEntity.status(HttpStatus.OK).body(userId)))
                .orElseGet(() -> CompletableFuture.supplyAsync(
                        () -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Required sportsmanId or coachId doesn't found")));

    }


    // NOTIFICATIONS
    @GetMapping("getNotifications/{userId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> getAllNotifications(@PathVariable(value = "userId") long userId) {
        return checkIdExistAndConvertListToFuture(
                userService.getUser(userId),
                notifService::getNotificationMessagesByUserId,
                String.class,
                "Required user doesn't found");
    }


//    // NOTIFICATIONS
//    @GetMapping("getRating/{sportsmanId}")
//    public @ResponseBody CompletableFuture<ResponseEntity<?>> getRating(@PathVariable(value = "sportsmanId") long sportsmanId) {
//        return checkIdExistAndConvertListToFuture(
//                userService.getUserAndCheckType(sportsmanId, User.Kind.sportsman),
//                userService::getRating,
//                UserDto.class,
//                "Required sportsman doesn't found");
//    }


    // FOLLOWERS
    @PostMapping("addSubscription/{userId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> addSubscription(@PathVariable(value = "userId") long userId, @RequestBody long followToId) {
        if (userService.addSubscription(userId, followToId)) {
            return CompletableFuture.supplyAsync(() ->
                    ResponseEntity.status(HttpStatus.OK).body(null));
        }
        return CompletableFuture.supplyAsync(() ->
                ResponseEntity.status(HttpStatus.NOT_FOUND).body("Required userId or followToId doesn't found"));
    }

    @GetMapping("getFollowers/{userId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> getFollowings(@PathVariable(value = "userId") long userId) {
        return checkIdExistAndConvertListToFuture(
                userService.getUser(userId),
                userService::getFollowers,
                UserDto.class,
                "Required user doesn't found");
    }

    @GetMapping("getSubscriptions/{userId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> getSubscriptions(@PathVariable(value = "userId") long userId) {
        return checkIdExistAndConvertListToFuture(
                userService.getUser(userId),
                userService::getSubscriptions,
                UserDto.class,
                "Required user doesn't found");
    }

    @GetMapping("searchSportsman/{searchString}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> searchSportsman(@PathVariable(value = "searchString") String searchString) {
        return CompletableFuture.supplyAsync(() -> ResponseEntity.status(HttpStatus.OK).body(
                userService.searchUser(searchString, User.Kind.sportsman).stream()
                        .map(user -> mapper.map(user, UserDto.class))
                        .toList()
        ));
    }



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

    @GetMapping("getAllTrainings/{sportsmanId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> getAllTrainingsByUserId(@PathVariable(value = "sportsmanId") long sportsmanId) {
        return checkIdExistAndConvertListToFuture(
                userService.getUserAndCheckType(sportsmanId, User.Kind.sportsman),
                user -> trainingService.findAllByUserId(user.getId()),
                TrainingDto.class,
                "Required training doesn't found");
    }

    @GetMapping("getAllExerciseByTrain/{trainId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> getAllExerciseByTrainId(@PathVariable(value = "trainId") long trainId) {
        return checkIdExistAndConvertListToFuture(
                trainingService.findById(trainId),
                Training::getExercises,
                ExerciseDto.class,
                "Required training doesn't found");
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

    @PostMapping("/markEventCompleted/{eventId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> editCompleted(@PathVariable(value = "eventId") long eventId, @RequestBody boolean completed) {
        return eventService.editEventCompleted(eventId, completed)
                .<CompletableFuture<ResponseEntity<?>>>map(idEvent -> CompletableFuture.supplyAsync(
                        () -> ResponseEntity.status(HttpStatus.OK).body(idEvent)))
                .orElseGet(() -> CompletableFuture.supplyAsync(
                        () -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Required eventId doesn't found")));
    }

    @GetMapping("getTrainingByEvent/{eventId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> getTrainingByEventId(@PathVariable(value = "eventId") long eventId) {
        return convertOptionalToFuture(eventService.findTrainingById(eventId), TrainingDto.class, "Required training doesn't found");
    }

    @GetMapping("getAllExerciseByEvent/{eventId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> getAllExerciseByEventId(@PathVariable(value = "eventId") long eventId) {
        return checkIdExistAndConvertListToFuture(
                eventService.findTrainingById(eventId),
                Training::getExercises,
                ExerciseDto.class,
                "Required event doesn't found");
    }

    @GetMapping("getEventIsCompleted/{eventId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> getEventIsCompleted(@PathVariable(value = "eventId") long eventId) {
        return convertOptionalToFuture(eventService.isCompeted(eventId), Boolean.class, "Event with this ID doesn't exist");
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
        return checkIdExistAndConvertListToFuture(
                userService.getUserAndCheckType(sportsmanId, User.Kind.sportsman),
                user -> planService.findAllPlansBySportsmanId(user.getId()),
                PlanDto.class,
                "Required sportsmanId doesn't found");
    }

    @GetMapping("getPlan/{planId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> getPlanByPlanId(@PathVariable(value = "planId") long planId) {
        return convertOptionalToFuture(planService.findPlanByPlanId(planId), PlanDto.class, "Plan with this ID doesn't exist");
    }

    @GetMapping("getAllNotCompletedPlans/{sportsmanId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> getAllNotCompletedPlans(@PathVariable(value = "sportsmanId") long sportsmanId) {
        return checkIdExistAndConvertListToFuture(
                userService.getUserAndCheckType(sportsmanId, User.Kind.sportsman),
                user -> planService.findAllNotCompletedPlansBySportsmanId(user.getId()),
                PlanDto.class,
                "Required sportsmanId doesn't found");
    }

    @GetMapping("getPlanIsCompleted/{planId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> getPlanIsCompleted(@PathVariable(value = "planId") long planId) {
        return convertOptionalToFuture(planService.isCompeted(planId), Boolean.class, "Plan with this ID doesn't exist");
    }

    private CompletableFuture<ResponseEntity<?>> convertOptionalToFuture(Optional<?> optionalResult, Class<?> returnType, String errorMessage) {
        return optionalResult.map(result -> CompletableFuture.supplyAsync(
                        () -> {
                            if (result.getClass().equals(returnType)) {
                                return ResponseEntity.status(HttpStatus.OK).body(mapper.map(result, returnType));
                            }
                            return ResponseEntity.status(HttpStatus.OK).body(result);
                        }))
                .orElseGet(() -> CompletableFuture.supplyAsync(
                        () -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage)));

    }
    private <T> CompletableFuture<ResponseEntity<?>> checkIdExistAndConvertListToFuture(Optional<T> optional, Function<T, List<?>> func, Class<?> itemReturnType, String errorMessage) {
        return optional.<CompletableFuture<ResponseEntity<?>>>map(result -> CompletableFuture.supplyAsync(
                        () -> ResponseEntity.status(HttpStatus.OK).body(func.apply(result).stream()
                                .map(item -> mapper.map(item, itemReturnType))
                                .toList())))
                .orElseGet(() -> CompletableFuture.supplyAsync(
                        () -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage)));
    }
}