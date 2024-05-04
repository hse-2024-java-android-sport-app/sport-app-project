package org.sportApp.repo;

import org.sportApp.entities.Exercise;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseRepository extends CrudRepository<Exercise, Long> {
}