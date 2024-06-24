package org.sportApp.repo;

import org.sportApp.entities.Plan;
import org.sportApp.entities.Subscriber;
import org.sportApp.entities.TrainingEvent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriberRepository extends CrudRepository<Subscriber, Long> {
}