package org.sportApp.repo;

import org.sportApp.entities.Training;
import org.sportApp.entities.TrainingEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Repository
public interface TrainingEventRepository extends CrudRepository<TrainingEvent, Long> {
    List<TrainingEvent> getTrainingEventsByCompletedTrueAndDateIs(Date date);

    int countByCompletedTrueAndDateAndTraining_UserId(Date date, long training_userId);
}