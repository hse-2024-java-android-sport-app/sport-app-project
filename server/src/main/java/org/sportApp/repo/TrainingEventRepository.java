package org.sportApp.repo;

import org.sportApp.entities.TrainingEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingEventRepository extends CrudRepository<TrainingEvent, Long> {
}