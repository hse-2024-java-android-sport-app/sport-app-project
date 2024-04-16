package org.sportApp.training;

import java.util.List;

public class TrainingDto {
    private long trainId;
    private List<ExerciseDto> exercises;
    private long userId;

    public TrainingDto() {
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
}
