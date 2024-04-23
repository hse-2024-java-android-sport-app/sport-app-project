package org.sportApp.repo;

import org.sportApp.entities.Training;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingRepository extends CrudRepository<Training, Long> {
    Iterable<Training> findAllByUserId(long userId);
}