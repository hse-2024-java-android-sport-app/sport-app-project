package org.sportApp.services;

import org.sportApp.entities.Exercise;
import org.sportApp.entities.Training;
import org.sportApp.entities.TrainingEvent;
import org.sportApp.repo.TrainingEventRepository;
import org.sportApp.repo.TrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EventService {
    private final TrainingEventRepository eventRepository;
    private final TrainingService trainingService;

    @Autowired
    public EventService(TrainingEventRepository eventRepository, TrainingService trainingService) {
        this.eventRepository = eventRepository;
        this.trainingService = trainingService;
    }


    public TrainingEvent saveEvent(TrainingEvent event) {
        Training training = event.getTraining();
        training.setEvent(event);
        training.getExercises().forEach(exr -> exr.setTraining(training));
        TrainingEvent savedEvent = eventRepository.save(event);
        trainingService.saveTraining(training);
        return savedEvent;
    }

    public Optional<Exercise> addExercise(long eventId, Exercise exercise) {
        Optional<TrainingEvent> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isPresent()) {
            return trainingService.addExercise(optionalEvent.get().getTraining().getTrainId(), exercise);
        }
        return Optional.empty();
    }

    public Optional<Training> findTrainingById(long eventId) {
        return eventRepository.findById(eventId).map(TrainingEvent::getTraining);
    }
}
