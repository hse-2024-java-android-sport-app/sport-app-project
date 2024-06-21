package org.sportApp.services;

import org.sportApp.entities.*;
import org.sportApp.repo.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanService {

    private final PlanRepository planRepository;
    private final EventService eventService;
    private final NotificationService notifService;

    @Autowired
    public PlanService(PlanRepository planRepository, EventService eventService, NotificationService notifService) {
        this.planRepository = planRepository;
        this.eventService = eventService;
        this.notifService = notifService;
    }


    public Plan savePlan(Plan plan) {
        if (plan.getTrainings() == null) {
            return planRepository.save(plan);
        }
        List<TrainingEvent> events = plan.getTrainings();
        plan.setTrainings(List.of());
        Plan savedPlan = planRepository.save(plan);
        events.forEach(event -> {
            event.setPlan(savedPlan);
            eventService.saveEvent(event);
        });
        notifService.sendPlan(savedPlan, "New plan \"" + savedPlan.getName() + "\" was created!");
        return savedPlan;
    }

    public Optional<TrainingEvent> addEvent(long planId, TrainingEvent event) {
        Optional<Plan> optionalPlan = planRepository.findById(planId);
        if (optionalPlan.isPresent()) {
            event.setPlan(optionalPlan.get());
            notifService.sendPlan(optionalPlan.get(), "New training event was added to plan \"" + optionalPlan.get().getName() + "\"");
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
