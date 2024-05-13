package org.sportApp.services;

import org.sportApp.entities.Exercise;
import org.sportApp.entities.Training;
import org.sportApp.repo.ExerciseRepository;
import org.sportApp.repo.TrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TrainingService {

    private final TrainingRepository trainingRepository;
    private final ExerciseRepository exerciseRepository;

    @Autowired
    public TrainingService(TrainingRepository trainingRepository, ExerciseRepository exerciseRepository) {
        this.trainingRepository = trainingRepository;
        this.exerciseRepository = exerciseRepository;
    }

    public Training saveTraining(Training training) {
        if (training.getExercises() == null) {
            return trainingRepository.save(training);
        }
        training.getExercises().forEach(exr -> exr.setTraining(training));
        Training savedTraining = trainingRepository.save(training);
        exerciseRepository.saveAll(training.getExercises());
        return savedTraining;
    }

    public Iterable<Training> findAllByUserId(long userId) {
        return trainingRepository.findAllByUserId(userId);
    }

    public Optional<Training> findById(long trainId) {
        return trainingRepository.findById(trainId);
    }

    public Optional<Exercise> addExercise(long trainId, Exercise exercise) {
        Optional<Training> optionalTraining = findById(trainId);
        if (optionalTraining.isPresent()) {
            exercise.setTraining(optionalTraining.get());
            return Optional.of(exerciseRepository.save(exercise));
        }
        return Optional.empty();
    }
}
