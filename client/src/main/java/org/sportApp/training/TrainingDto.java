package org.sportApp.training;

import java.io.Serializable;
import java.util.List;

public class TrainingDto implements Serializable {
    private String name;
    private long trainId;
    private List<ExerciseDto> exercises;
    private long userId;

    public TrainingDto() {
    }

    public TrainingDto(String name, List<ExerciseDto> exercises) {
        this.name = name;
        this.exercises = exercises;
    }

    public List<ExerciseDto> getExercises() {
        return exercises;
    }

    public void setExercises(List<ExerciseDto> exercises) {
        this.exercises = exercises;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getTrainId() {
        return trainId;
    }

    public void setTrainId(long trainId) {
        this.trainId = trainId;
    }


    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

}
