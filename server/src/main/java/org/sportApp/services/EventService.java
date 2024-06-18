package org.sportApp.services;

import org.sportApp.entities.*;
import org.sportApp.repo.TrainingEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventService {
    private final TrainingEventRepository eventRepository;
    private final TrainingService trainingService;
    private final NotificationService notifService;

    @Autowired
    public EventService(TrainingEventRepository eventRepository, TrainingService trainingService, NotificationService notifService) {
        this.eventRepository = eventRepository;
        this.trainingService = trainingService;
        this.notifService = notifService;
    }


    public TrainingEvent saveEvent(TrainingEvent event) {
        Training training = event.getTraining();
        training.getExercises().forEach(exr -> exr.setTrainingDto(training));
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
        if(event.isPresent()){
            event.get().setCompleted(completed);
            notifService.sendEventCompleted(event.get());
            return Optional.of(eventRepository.save(event.get()).getEventId());
        }
        return Optional.empty();
    }

    public Optional<Training> findTrainingById(long eventId) {
        return eventRepository.findById(eventId).map(TrainingEvent::getTraining);
    }

    public Optional<Boolean> isCompeted(long eventId) {
        return eventRepository.findById(eventId).map(TrainingEvent::isCompleted);
    }
}
