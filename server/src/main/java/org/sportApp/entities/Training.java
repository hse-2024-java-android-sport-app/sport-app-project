package org.sportApp.entities;

import java.util.List;
import java.util.Locale;

import jakarta.persistence.*;

@Entity
@Table(name="Trainings")
public class Training {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long trainId;
    @OneToMany(mappedBy="trainingDto", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Exercise> exercises;
    private long userId;
    private int hours;
    private int minutes;
    private int seconds;


    protected Training() {}

    public long getTrainId() {
        return trainId;
    }

    public void setTrainId(long trainId) {
        this.trainId = trainId;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Training{" +
                "trainId=" + trainId +
                ", exercises=" + exercises +
                ", userId=" + userId +
                '}';
    }

    public Training(long userId){
        this.userId = userId;
        this.exercises = List.of();
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
}
