package org.sportApp.services;

import org.sportApp.entities.Exercise;
import org.sportApp.entities.Training;
import org.sportApp.repo.TrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TrainingService {

    private final TrainingRepository trainingRepository;

    @Autowired
    public TrainingService(TrainingRepository trainingRepository) {
        this.trainingRepository = trainingRepository;
    }

    public Training saveTraining(Training training) {
        //FUTURE add password encoding
        return trainingRepository.save(training);
    }

    public Iterable<Training> findAllByUserId(long userId) {
        return trainingRepository.findAllByUserId(userId);
    }

    public Optional<Training> findById(long trainId) {
        return trainingRepository.findById(trainId);
    }
}
