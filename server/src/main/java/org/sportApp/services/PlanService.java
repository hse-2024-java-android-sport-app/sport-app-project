package org.sportApp.services;

import org.sportApp.entities.Exercise;
import org.sportApp.entities.Plan;
import org.sportApp.entities.Training;
import org.sportApp.entities.TrainingEvent;
import org.sportApp.repo.PlanRepository;
import org.sportApp.repo.TrainingEventRepository;
import org.sportApp.repo.TrainingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PlanService {

    private final PlanRepository planRepository;
    private final EventService eventService;

    @Autowired
    public PlanService(PlanRepository planRepository, EventService eventService) {
        this.planRepository = planRepository;
        this.eventService = eventService;
    }


    public Plan savePlan(Plan plan) {
        if (plan.getTrainings() == null) {
            return planRepository.save(plan);
        }
        plan.getTrainings().forEach(train -> train.setPlan(plan));
        Plan savedPlan = planRepository.save(plan);
        plan.getTrainings().forEach(eventService::saveEvent);
        return savedPlan;
    }

    public Optional<TrainingEvent> addEvent(long planId, TrainingEvent event) {
        Optional<Plan> optionalPlan = planRepository.findById(planId);
        if (optionalPlan.isPresent()) {
            event.setPlan(optionalPlan.get());
            return Optional.of(eventService.saveEvent(event));
        }
        return Optional.empty();
    }

    public List<Plan> findAllPlansBySportsmanId(long sportsmanId) {
        return planRepository.findBySportsmanId(sportsmanId);
    }

    public List<Plan> findAllNotCompletedPlansBySportsmanId(long sportsmanId) {
        return planRepository.findBySportsmanId(sportsmanId).stream().filter(Plan::isCompleted).toList();
    }

    public Optional<Plan> findPlanByPlanId(long planId) {
        return planRepository.findById(planId);
    }

    public Optional<Boolean> isCompeted(long planId) {
        return planRepository.findById(planId).map(Plan::isCompleted);
    }
}
