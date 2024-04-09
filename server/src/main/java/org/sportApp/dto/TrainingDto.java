package org.sportApp.dto;

import java.util.List;

public class TrainingDto {
    private int trainId;
    private List<ExerciseDto> exercises;
    private int userId;

    public TrainingDto() {
    }

    public List<ExerciseDto> getExercises() {
        return exercises;
    }
}
