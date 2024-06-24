package org.sportApp.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlanDto implements Serializable {
    private long planId;
    private String name;
    private List<TrainingEventDto> trainings = new ArrayList<>();
    private long coachId;
    private long sportsmanId;

    private LocalDate creationTime;

    private boolean isCompleted;
    public PlanDto() {
        //BackendService.createPlan(this);
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        this.isCompleted = isCompleted;
    }

    private void edit(){}

    private void delete(){}
    private void push(){}

    public void setTraining(TrainingEventDto trainingEventDto) {
        trainings.add(trainingEventDto);
    }

    public List<TrainingEventDto> getTrainings() {
        return trainings;
    }

    public long getPlanId() {
        return planId;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
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

    public void setTrainings(List<TrainingEventDto> trainings) {
        this.trainings = trainings;
    }

    public LocalDate getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDate creationTime) {
        this.creationTime = creationTime;
    }
}
