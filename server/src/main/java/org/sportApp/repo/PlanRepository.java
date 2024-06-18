package org.sportApp.repo;

import org.sportApp.entities.Plan;
import org.sportApp.entities.TrainingEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends CrudRepository<Plan, Long> {
    List<Plan> findBySportsmanId(long sportsmanId);
}