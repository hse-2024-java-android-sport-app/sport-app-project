package org.sportApp.services;

import org.modelmapper.ModelMapper;
import org.sportApp.entities.*;
import org.sportApp.repo.TrainingEventRepository;
import org.sportApp.repo.TrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.events.Event;

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
        training.getExercises().forEach(exr -> exr.setTraining(training));
        if (trainingService.findById(training.getTrainId()).isEmpty()) {
            trainingService.saveTraining(training);
        }
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
