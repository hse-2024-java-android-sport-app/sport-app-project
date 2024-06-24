package org.sportApp.dto;

import java.util.List;

public class TrainingDto {
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

    public long getTrainId() {
        return trainId;
    }

    public void setTrainId(long trainId) {
        this.trainId = trainId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setExercises(List<ExerciseDto> exercises) {
        this.exercises = exercises;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    @Override
    public String toString() {
        return "TrainingDto{" +
                "trainId=" + trainId +
                ", exercises=" + exercises +
                ", userId=" + userId +
                ", hours=" + hours +
                ", minutes=" + minutes +
                ", seconds=" + seconds +
                '}';
    }
}
