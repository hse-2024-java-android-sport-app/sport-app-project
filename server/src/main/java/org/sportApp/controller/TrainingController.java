//package org.sportApp.controller;
//
//import org.sportApp.entities.*;
//import org.sportApp.services.*;
//import org.sportApp.dto.*;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.modelmapper.*;
//
//import java.util.Optional;
//import java.util.concurrent.CompletableFuture;
//
//@RestController
//@RequestMapping("/sport_app")
//public class TrainingController {
//    private final UserService userService;
//    private final TrainingService trainingService;
//    private final EventService eventService;
//    private final PlanService planService;
//    private final ModelMapper mapper = new ModelMapper();
//
//    @Autowired
//    public TrainingController(UserService userService, TrainingService trainingService, EventService eventService, PlanService planService) {
//        this.userService = userService;
//        this.trainingService = trainingService;
//        this.eventService = eventService;
//        this.planService = planService;
//    }
//
//    // TRAINING
//    @PostMapping("createTraining")
//    public @ResponseBody CompletableFuture<ResponseEntity<?>> createTraining(@RequestBody TrainingDto trainingDto) {
//        Training training = this.mapper.map(trainingDto, Training.class);
//
//        Training savedTraining = trainingService.saveTraining(training);
//        CompletableFuture<Training> future = CompletableFuture.supplyAsync(() -> savedTraining);
//        if (!savedTraining.equals(training)) {
//            return future.thenApply(result -> ResponseEntity.status(HttpStatus.VARIANT_ALSO_NEGOTIATES).body("Saved training is different from required"));
//        }
//        return future.thenApply(result -> ResponseEntity.status(HttpStatus.CREATED).body(savedTraining.getTrainId()));
//    }
//
//    @PostMapping("addExerciseByTrain/{trainId}")
//    public @ResponseBody CompletableFuture<ResponseEntity<?>> addExerciseByTrain(@PathVariable(value = "trainId") long trainId, @RequestBody ExerciseDto exerciseDto) {
//        Exercise exercise = this.mapper.map(exerciseDto, Exercise.class);
//
//        Optional<Exercise> addedExercise = trainingService.addExercise(trainId, exercise);
//        CompletableFuture<Optional<Exercise>> future = CompletableFuture.supplyAsync(() -> addedExercise);
//        if (addedExercise.isEmpty()) {
//            return future.thenApply(result -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Training with this ID doesn't exist"));
//        }
//        if (!addedExercise.get().equals(exercise)) {
//            return future.thenApply(result -> ResponseEntity.status(HttpStatus.VARIANT_ALSO_NEGOTIATES).body("Saved exercise is different from required"));
//        }
//        return future.thenApply(result -> ResponseEntity.status(HttpStatus.CREATED).body(addedExercise.get().getId()));
//    }
//
//    @GetMapping("getAllTrainings/{userId}")
//    public @ResponseBody CompletableFuture<ResponseEntity<?>> getAllTrainingsByUserId(@PathVariable(value = "userId") long userId) {
//        if (userService.notExistsById(userId)) {
//            return CompletableFuture.supplyAsync(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Required userId doesn't found"));
//        }
//        return CompletableFuture.supplyAsync(() -> ResponseEntity.status(HttpStatus.OK).body(
//                trainingService.findAllByUserId(userId).stream()
//                        .map(training -> mapper.map(training, TrainingDto.class))
//                        .toList()));
//    }
//
//    @GetMapping("getAllExerciseByTrain/{trainId}")
//    public @ResponseBody CompletableFuture<ResponseEntity<?>> getAllExerciseByTrainId(@PathVariable(value = "trainId") long trainId) {
//        return trainingService.findById(trainId)
//                .<CompletableFuture<ResponseEntity<?>>>map(training -> CompletableFuture.supplyAsync(
//                        () -> ResponseEntity.status(HttpStatus.OK).body(
//                                training.getExercises().stream()
//                                        .map(exr -> this.mapper.map(exr, ExerciseDto.class))
//                                        .toList())))
//                .orElseGet(() -> CompletableFuture.supplyAsync(
//                        () -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Required training doesn't found")));
//    }
//
//
//
//    // EVENT
//    @PostMapping("addExerciseByEvent/{eventId}")
//    public @ResponseBody CompletableFuture<ResponseEntity<?>> addExerciseByEvent(@PathVariable(value = "eventId") long eventId, @RequestBody ExerciseDto exerciseDto) {
//        Exercise exercise = this.mapper.map(exerciseDto, Exercise.class);
//
//        Optional<Exercise> addedExercise = eventService.addExercise(eventId, exercise);
//        CompletableFuture<Optional<Exercise>> future = CompletableFuture.supplyAsync(() -> addedExercise);
//        if (addedExercise.isEmpty()) {
//            return future.thenApply(result -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event with this ID doesn't exist"));
//        }
//        if (!addedExercise.get().equals(exercise)) {
//            return future.thenApply(result -> ResponseEntity.status(HttpStatus.VARIANT_ALSO_NEGOTIATES).body("Saved exercise is different from required"));
//        }
//        return future.thenApply(result -> ResponseEntity.status(HttpStatus.CREATED).body(addedExercise.get().getId()));
//    }
//
//    @PostMapping("/markEventCompleted/{eventId}")
//    public @ResponseBody CompletableFuture<ResponseEntity<?>> editCompleted(@PathVariable(value = "eventId") long eventId, @RequestBody boolean completed) {
//        return eventService.editEventCompleted(eventId, completed)
//                .<CompletableFuture<ResponseEntity<?>>>map(idEvent -> CompletableFuture.supplyAsync(
//                        () -> ResponseEntity.status(HttpStatus.OK).body(idEvent)))
//                .orElseGet(() -> CompletableFuture.supplyAsync(
//                        () -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Required eventId doesn't found")));
//    }
//
//    @GetMapping("getTrainingByEvent/{eventId}")
//    public @ResponseBody CompletableFuture<ResponseEntity<?>> getTrainingByEventId(@PathVariable(value = "eventId") long eventId) {
//        return eventService.findTrainingById(eventId)
//                .<CompletableFuture<ResponseEntity<?>>>map(training -> CompletableFuture.supplyAsync(
//                        () -> ResponseEntity.status(HttpStatus.OK).body(mapper.map(training, TrainingDto.class))))
//                .orElseGet(() -> CompletableFuture.supplyAsync(
//                        () -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Required training doesn't found")));
//    }
//
//    @GetMapping("getAllExerciseByEvent/{eventId}")
//    public @ResponseBody CompletableFuture<ResponseEntity<?>> getAllExerciseByEventId(@PathVariable(value = "eventId") long eventId) {
//        return eventService.findTrainingById(eventId)
//                .<CompletableFuture<ResponseEntity<?>>>map(training -> CompletableFuture.supplyAsync(
//                        () -> ResponseEntity.status(HttpStatus.OK).body(
//                                training.getExercises().stream()
//                                        .map(exr -> this.mapper.map(exr, ExerciseDto.class))
//                                        .toList())))
//                .orElseGet(() -> CompletableFuture.supplyAsync(
//                        () -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Required training doesn't found")));
//    }
//
//    @GetMapping("getEventIsCompleted/{eventId}")
//    public @ResponseBody CompletableFuture<ResponseEntity<?>> getEventIsCompleted(@PathVariable(value = "eventId") long eventId) {
//        return eventService.isCompeted(eventId)
//                .<CompletableFuture<ResponseEntity<?>>>map(isCompleted -> CompletableFuture.supplyAsync(
//                        () -> ResponseEntity.status(HttpStatus.OK).body(isCompleted)))
//                .orElseGet(() -> CompletableFuture.supplyAsync(
//                        () -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Event with this ID doesn't exist")));
//    }
//
//
//    // PLAN
//    @PostMapping("createPlan")
//    public @ResponseBody CompletableFuture<ResponseEntity<?>> createPlan(@RequestBody PlanDto planDto) {
//        Plan plan = this.mapper.map(planDto, Plan.class);
//
//        Plan savedPlan = planService.savePlan(plan);
//        CompletableFuture<Plan> future = CompletableFuture.supplyAsync(() -> savedPlan);
//        if (!savedPlan.equals(plan)) {
//            return future.thenApply(result -> ResponseEntity.status(HttpStatus.VARIANT_ALSO_NEGOTIATES).body("Saved plan is different from required"));
//        }
//        return future.thenApply(result -> ResponseEntity.status(HttpStatus.CREATED).body(savedPlan.getPlanId()));
//    }
//
//    @PostMapping("addEvent/{planId}")
//    public @ResponseBody CompletableFuture<ResponseEntity<?>> addEventByPlanId(@PathVariable(value = "planId") long planId, @RequestBody TrainingEventDto eventDto) {
//        TrainingEvent event = this.mapper.map(eventDto, TrainingEvent.class);
//
//        Optional<TrainingEvent> addedEvent = planService.addEvent(planId, event);
//        CompletableFuture<Optional<TrainingEvent>> future = CompletableFuture.supplyAsync(() -> addedEvent);
//        if (addedEvent.isEmpty()) {
//            return future.thenApply(result -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plan with this ID doesn't exist"));
//        }
//        if (!addedEvent.get().equals(event)) {
//            return future.thenApply(result -> ResponseEntity.status(HttpStatus.VARIANT_ALSO_NEGOTIATES).body("Saved event is different from required"));
//        }
//        return future.thenApply(result -> ResponseEntity.status(HttpStatus.CREATED).body(addedEvent.get().getEventId()));
//    }
//
//    @GetMapping("getAllPlans/{sportsmanId}")
//    public @ResponseBody CompletableFuture<ResponseEntity<?>> getAllPlansBySportsmanId(@PathVariable(value = "sportsmanId") long sportsmanId) {
//        return CompletableFuture.supplyAsync(
//                () -> ResponseEntity.status(HttpStatus.OK).body(
//                        planService.findAllPlansBySportsmanId(sportsmanId).stream()
//                                .map(plan -> mapper.map(plan, PlanDto.class))
//                                .toList()));
//    }
//
//    @GetMapping("getPlan/{planId}")
//    public @ResponseBody CompletableFuture<ResponseEntity<?>> getPlanByPlanId(@PathVariable(value = "planId") long planId) {
//        return planService.findPlanByPlanId(planId)
//                .<CompletableFuture<ResponseEntity<?>>>map(plan -> CompletableFuture.supplyAsync(
//                        () -> ResponseEntity.status(HttpStatus.OK).body(mapper.map(plan, PlanDto.class))))
//                .orElseGet(() -> CompletableFuture.supplyAsync(
//                        () -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plan with this ID doesn't exist")));
//
//    }
//
//    @GetMapping("getAllNotCompletedPlans/{sportsmanId}")
//    public @ResponseBody CompletableFuture<ResponseEntity<?>> getAllNotCompletedPlans(@PathVariable(value = "sportsmanId") long sportsmanId) {
//        return CompletableFuture.supplyAsync(
//                () -> ResponseEntity.status(HttpStatus.OK).body(
//                        planService.findAllNotCompletedPlansBySportsmanId(sportsmanId).stream()
//                                .map(plan -> mapper.map(plan, PlanDto.class))
//                                .toList()));
//    }
//
//    @GetMapping("getPlanIsCompleted/{planId}")
//    public @ResponseBody CompletableFuture<ResponseEntity<?>> getPlanIsCompleted(@PathVariable(value = "planId") long planId) {
//        return planService.isCompeted(planId)
//                .<CompletableFuture<ResponseEntity<?>>>map(isCompleted -> CompletableFuture.supplyAsync(
//                        () -> ResponseEntity.status(HttpStatus.OK).body(isCompleted)))
//                .orElseGet(() -> CompletableFuture.supplyAsync(
//                        () -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Plan with this ID doesn't exist")));
//    }
//}