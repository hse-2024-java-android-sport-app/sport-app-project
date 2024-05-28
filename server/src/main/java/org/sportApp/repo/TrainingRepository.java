package org.sportApp.repo;

import org.sportApp.entities.Training;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRepository extends CrudRepository<Training, Long> {
    List<Training> findAllByUserId(long userId);
}