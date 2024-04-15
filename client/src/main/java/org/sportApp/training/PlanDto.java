package org.sportApp.training;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PlanDto implements Serializable {
    private final List<TrainingEventDto> trainings = new ArrayList<>();
    private long userId;
    private long id;

    private boolean isCompleted;

    public PlanDto() {
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
}
