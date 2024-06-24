package org.sportApp.services;

import org.sportApp.entities.*;
import org.sportApp.repo.TrainingEventRepository;
import org.sportApp.repo.TrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class EventService {
    private final TrainingEventRepository eventRepository;
    private final TrainingService trainingService;
    private final TrainingRepository trainingRepository;
    private final NotificationService notifService;
    private final UserService userService;

    @Autowired
    public EventService(TrainingEventRepository eventRepository, TrainingService trainingService, TrainingRepository trainingRepository, NotificationService notifService, UserService userService) {
        this.eventRepository = eventRepository;
        this.trainingService = trainingService;
        this.trainingRepository = trainingRepository;
        this.notifService = notifService;
        this.userService = userService;
    }


    public TrainingEvent saveEvent(TrainingEvent event) {
        if (event.getDate() != null) {
            LocalDate localDate = event.getDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
            event.setDate(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }

        Training training = event.getTraining();
        training.setTrainId(0);
        Training savedTraining = trainingService.saveTraining(training);
        event.setTraining(savedTraining);
        return eventRepository.save(event);
    }

    public Optional<Exercise> addExercise(long eventId, Exercise exercise) {
        Optional<TrainingEvent> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isPresent()) {
            return trainingService.addExercise(optionalEvent.get().getTraining().getTrainId(), exercise);
        }
        return Optional.empty();
    }

    public Optional<Long> editEventCompleted(long eventId, boolean completed) {
        Optional<TrainingEvent> event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            return Optional.empty();
        }
        if (event.get().isCompleted() == completed) {
            return Optional.of(eventId);
        }

        event.get().setCompleted(completed);
        notifService.sendEventCompleted(event.get());
        Date weekAgo = Date.from(LocalDate.now().minusWeeks(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        if (event.get().getDate() == null || event.get().getDate().before(weekAgo)) {
            // training isn't from this week. No influence for rating
            System.out.println("TIME: not this week: Date: " + event.get().getDate());
            return Optional.of(eventRepository.save(event.get()).getEventId());
        }

        int updateRatingFor = 0;
        long userId = event.get().getTraining().getUserId();
        int countSameDaySameUserCompletedTrainings = eventRepository.countByCompletedTrueAndDateAndTraining_UserId(
                event.get().getDate(), userId);
        if (completed && countSameDaySameUserCompletedTrainings == 0) {
            updateRatingFor = +10;
        } else if (!completed && countSameDaySameUserCompletedTrainings == 1) {
            updateRatingFor = -10;
        }
        System.out.println("TIME: this week: " + updateRatingFor);
        LocalDate localDate = event.get().getDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        event.get().setDate(Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        System.out.println("TIME: for training: " + event.get().getDate());
        return userService.editRating(userId, updateRatingFor) ? Optional.of(eventRepository.save(event.get()).getEventId()) : Optional.empty();
    }

    public Optional<Training> findTrainingById(long eventId) {
        return eventRepository.findById(eventId).map(TrainingEvent::getTraining);
    }

    public Optional<Boolean> isCompeted(long eventId) {
        return eventRepository.findById(eventId).map(TrainingEvent::isCompleted);
    }
}
