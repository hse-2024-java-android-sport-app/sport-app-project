package org.sportApp.training;

import org.sportApp.requests.BackendService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlanDto implements Serializable {
    private final List<TrainingEventDto> trainings = new ArrayList<>();
    private long userId;
    private long id;

    private static boolean isCompleted;

    public PlanDto() {
        BackendService backendService = new BackendService();
        backendService.createPlan(this);
    }

    public static boolean isCompleted() {
        return isCompleted;
    }

    public long getUserId() {
        return userId;
    }

    private void edit(){}

    private void delete(){}
    private void push(){}

    public void setTraining(TrainingEventDto trainingEventDto) {
        trainings.add(trainingEventDto);
    }

    public long getId() {
        return id;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setId(long id) {
        this.id = id;
    }
}
