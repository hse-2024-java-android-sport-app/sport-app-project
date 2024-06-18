package org.sportApp.dto;

import java.io.Serializable;
import java.util.List;

public class TrainingDto implements Serializable {
    private long trainId;
    private List<ExerciseDto> exercises;
    private long userId;

    private int hours;
    private int minutes;
    private int seconds;

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


    public void setTrainId(long trainId) {
        this.trainId = trainId;
    }

    public void calculateDuration() {
        int totalDuration = 0;
        for (ExerciseDto exercise : exercises) {
            totalDuration += exercise.getDuration() * exercise.getRepetitions();
        }

        hours = totalDuration / 3600;
        minutes = (totalDuration % 3600) / 60;
        seconds = totalDuration % 60;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }


}
