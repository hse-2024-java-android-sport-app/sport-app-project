package org.sportApp.services;

import org.sportApp.entities.Exercise;
import org.sportApp.entities.TrainingEvent;
import org.sportApp.repo.ExerciseRepository;
import org.sportApp.repo.TrainingEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TrainingEventService {

    private final TrainingEventRepository trainingEventRepository;
    private final ExerciseRepository exerciseRepository;

    @Autowired
    public TrainingEventService(TrainingEventRepository trainingEventRepository, ExerciseRepository exerciseRepository) {
        this.trainingEventRepository = trainingEventRepository;
        this.exerciseRepository = exerciseRepository;
    }

    public TrainingEvent saveTrainingEvent(TrainingEvent trainingEvent) {
        trainingEvent.getExercises().forEach(exr -> exr.setTrainingEvent(trainingEvent));
        TrainingEvent savedTrainingEvent = trainingEventRepository.save(trainingEvent);
        exerciseRepository.saveAll(trainingEvent.getExercises());
        return savedTrainingEvent;
    }

    public Iterable<TrainingEvent> findAllByUserId(long userId) {
        return trainingEventRepository.findAllByUserId(userId);
    }

    public Optional<TrainingEvent> findById(long trainId) {
        return trainingEventRepository.findById(trainId);
    }

    public Optional<Exercise> addExercise(long trainId, Exercise exercise) {
        Optional<TrainingEvent> optionalTrainingEvent = findById(trainId);
        if (optionalTrainingEvent.isPresent()) {
            exercise.setTrainingEvent(optionalTrainingEvent.get());
            return Optional.of(exerciseRepository.save(exercise));
        }
        return Optional.empty();
    }
}
