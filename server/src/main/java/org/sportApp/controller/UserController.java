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
import java.util.function.BiFunction;
import java.util.function.Function;


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
        mapper.createTypeMap(User.class, UserDto.class)
                .addMappings(m -> {
                    m.skip(UserDto::setCoachId);
                    m.skip(UserDto::setPassword);
                }).setPostConverter(
                        context -> {
                            User source = context.getSource();
                            context.getDestination().setCoachId(
                                    (source == null || source.getCoach() == null) ? 0 : source.getCoach().getId());
                            return context.getDestination();
                        });
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

        return createEntity(userDto, User.class, userService::registerUser, User::getId);
    }

    @PostMapping("authorization")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> authorizeUser(@RequestBody UserDto userDto) {
        if (!userService.existsByLogin(userDto.getLogin())) {
            return CompletableFuture.supplyAsync(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User with this login doesn't exist"));
        }
        return convertOptionalToFuture(
                userService.authorizeUser(userDto.getLogin(), userDto.getPassword()),
                User::getId,
                "User with this password doesn't exist");
    }

    @GetMapping("getUserType/{userId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> getUserType(@PathVariable(value = "userId") long userId) {
        return convertOptionalToFuture(userService.getUserType(userId), source -> mapper.map(source, UserDto.Kind.class), "Required userId doesn't found");
    }

    @GetMapping("getUserById/{userId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> getUser(@PathVariable(value = "userId") long userId) {
        return convertOptionalToFuture(userService.getUser(userId), source -> mapper.map(source, UserDto.class), "Required userId doesn't found");
    }

    @GetMapping("getSportsmenByCoachId/{coachId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> getSportsmenByCoachId(@PathVariable(value = "coachId") long coachId) {
        return checkIdExistAndConvertListToFuture(
                userService.getUserAndCheckType(coachId, User.Kind.coach),
                coach -> userService.getAllSportsmenByCoachId(coach.getId()),
                UserDto.class,
                "Required coach doesn't found");
    }

    @GetMapping("getIsCoachSet/{sportsmanId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> getIsCoachSet(@PathVariable(value = "sportsmanId") long sportsmanId) {
        return convertOptionalToFuture(
                userService.getUserAndCheckType(sportsmanId, User.Kind.sportsman)
                        .map(user -> user.getCoach() == null),
                Function.identity(),
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
        return convertOptionalToFuture(
                userService.editCoach(sportsmanId, coachId),
                Function.identity(),
                "Required sportsmanId or coachId doesn't found"
        );
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


    // RATING
    @GetMapping("getRating/{sportsmanId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> getRating(@PathVariable(value = "sportsmanId") long sportsmanId) {
        return checkIdExistAndConvertListToFuture(
                userService.getUserAndCheckType(sportsmanId, User.Kind.sportsman),
                userService::getRating,
                UserDto.class,
                "Required sportsman doesn't found");
    }


    // FOLLOWERS
    @PostMapping("addSubscription/{userId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> addSubscription(@PathVariable(value = "userId") long userId, @RequestBody long followToId) {
        return convertOptionalToFuture(
                userService.addSubscription(userId, followToId),
                result -> null,
                "Required userId or followToId doesn't found"
        );
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
        return createEntity(trainingDto, Training.class, trainingService::saveTraining, Training::getTrainId);
    }

    @PostMapping("addExerciseByTrain/{trainId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> addExerciseByTrain(@PathVariable(value = "trainId") long trainId, @RequestBody ExerciseDto exerciseDto) {
        return addEntityToContainer(trainId, exerciseDto, Exercise.class, trainingService::addExercise,
                Exercise::getId, "Training with this ID doesn't exist");
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
        return addEntityToContainer(eventId, exerciseDto, Exercise.class, eventService::addExercise,
                Exercise::getId, "Event with this ID doesn't exist");
    }

    @PostMapping("/markEventCompleted/{eventId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> editCompleted(@PathVariable(value = "eventId") long eventId, @RequestBody boolean completed) {
        return convertOptionalToFuture(
                eventService.editEventCompleted(eventId, completed),
                Function.identity(),
                "Required eventId doesn't found"
        );
    }

    @GetMapping("getTrainingByEvent/{eventId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> getTrainingByEventId(@PathVariable(value = "eventId") long eventId) {
        return convertOptionalToFuture(eventService.findTrainingById(eventId), source -> mapper.map(source, TrainingDto.class), "Required training doesn't found");
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
        return convertOptionalToFuture(eventService.isCompeted(eventId), Function.identity(), "Event with this ID doesn't exist");
    }


    // PLAN
    @PostMapping("createPlan")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> createPlan(@RequestBody PlanDto planDto) {
        return createEntity(planDto, Plan.class, planService::savePlan, Plan::getPlanId);
    }

    @PostMapping("addEvent/{planId}")
    public @ResponseBody CompletableFuture<ResponseEntity<?>> addEventByPlanId(@PathVariable(value = "planId") long planId, @RequestBody TrainingEventDto eventDto) {
        return addEntityToContainer(planId, eventDto, TrainingEvent.class, planService::addEvent,
                TrainingEvent::getEventId, "Plan with this ID doesn't exist");
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
        return convertOptionalToFuture(planService.findPlanByPlanId(planId), source -> mapper.map(source, PlanDto.class), "Plan with this ID doesn't exist");
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
        return convertOptionalToFuture(
                planService.isCompeted(planId),
                Function.identity(),
                "Plan with this ID doesn't exist");
    }

    private <T> CompletableFuture<ResponseEntity<?>> convertOptionalToFuture(
            Optional<T> optionalResult, Function<T, ?> returnFunction, String errorMessage) {
        return optionalResult.<CompletableFuture<ResponseEntity<?>>>map(result -> CompletableFuture.supplyAsync(
                        () -> ResponseEntity.status(HttpStatus.OK).body(returnFunction.apply(result))))
                .orElseGet(() -> CompletableFuture.supplyAsync(
                        () -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage)));
    }


    private <T> CompletableFuture<ResponseEntity<?>> checkIdExistAndConvertListToFuture(
            Optional<T> optional, Function<T, List<?>> func, Class<?> itemReturnType, String errorMessage) {
        return optional.<CompletableFuture<ResponseEntity<?>>>map(result -> CompletableFuture.supplyAsync(
                        () -> ResponseEntity.status(HttpStatus.OK).body(func.apply(result).stream()
                                .map(item -> mapper.map(item, itemReturnType))
                                .toList())))
                .orElseGet(() -> CompletableFuture.supplyAsync(
                        () -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage)));
    }

    private <T, U> CompletableFuture<ResponseEntity<?>> addEntityToContainer(
            long addToId, T dto, Class<U> entityClass, BiFunction<Long, U, Optional<U>> function,
            Function<U, ?> returnFunc, String notFoundMessage) {
        return function.apply(addToId, this.mapper.map(dto, entityClass))
                .<CompletableFuture<ResponseEntity<?>>>map(addedEntity ->
                        CompletableFuture.supplyAsync(
                                () -> ResponseEntity.status(HttpStatus.CREATED).body(returnFunc.apply(addedEntity))))
                .orElseGet(() ->
                        CompletableFuture.supplyAsync(
                                () -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundMessage)));
    }

    private <T, U> CompletableFuture<ResponseEntity<?>> createEntity(
            T dto, Class<U> entityClass, Function<U, U> saveFunction, Function<U, ?> returnFunction) {
        U entity = this.mapper.map(dto, entityClass);
        U savedEntity = saveFunction.apply(entity);
        if (!savedEntity.equals(entity)) {
            return CompletableFuture.supplyAsync(
                    () -> ResponseEntity
                            .status(HttpStatus.VARIANT_ALSO_NEGOTIATES)
                            .body("Saved " + entityClass.getName() + " is different from required"));
        }
        return CompletableFuture.supplyAsync(
                () -> ResponseEntity.status(HttpStatus.CREATED).body(returnFunction.apply(savedEntity)));

    }
}