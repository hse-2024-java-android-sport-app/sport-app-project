package org.sportApp.dto;

import org.sportApp.dto.ExerciseDto;

import java.io.Serializable;
import java.util.List;

public class TrainingDto implements Serializable {
    private long trainId;
    private List<ExerciseDto> exercises;
    private long userId;

    public TrainingDto() {
    }

    public TrainingDto(String name, List<ExerciseDto> exercises) {
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

}
