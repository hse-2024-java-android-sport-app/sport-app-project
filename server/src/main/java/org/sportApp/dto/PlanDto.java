package org.sportApp.dto;

import org.sportApp.entities.TrainingEvent;

import java.util.ArrayList;
import java.util.List;

public class PlanDto {
    private long planId;
    private String name;
    private List<TrainingEventDto> trainings = new ArrayList<>();
    private long coachId;
    private long sportsmanId;

    public PlanDto(long id, String name, long coachId, long sportsmanId) {
        this.planId = id;
        this.name = name;
        this.coachId = coachId;
        this.sportsmanId = sportsmanId;
    }

    protected PlanDto(){}

    private boolean isCompleted(){
        return trainings.stream().allMatch(TrainingEventDto::isCompleted);
    }

    private void edit(){}
    private void delete(){}
    private void push(){}

    public long getPlanId() {
        return planId;
    }

    public void setPlanId(long id) {
        this.planId = id;
    }

    public long getCoachId() {
        return coachId;
    }

    public void setCoachId(long coachId) {
        this.coachId = coachId;
    }

    public long getSportsmanId() {
        return sportsmanId;
    }

    public void setSportsmanId(long sportsmanId) {
        this.sportsmanId = sportsmanId;
    }

    public boolean addTrainingEvent(TrainingEventDto trainingEventDto) {
        return trainings.add(trainingEventDto);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TrainingEventDto> getTrainings() {
        return this.trainings;
    }

    public void setTrainings(List<TrainingEventDto> trainings) {
        this.trainings = trainings;
    }
}
