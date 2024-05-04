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
    @OneToMany(mappedBy="training", cascade = CascadeType.ALL)
    private List<Exercise> exercises;
    private long userId;

    public Training(long trainId, List<Exercise> exercises, long userId) {
        this.trainId = trainId;
        this.exercises = exercises;
        this.userId = userId;
    }

    protected Training() {}

    public long getTrainId() {
        return this.trainId;
    }

    public void setTrainId(long trainId) {
        this.trainId = trainId;
    }

    public List<Exercise> getExercises() {
        return this.exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return String.format(Locale.GERMANY,
                "Training[id=%d, exercises='%s', userId='%d']",
                this.trainId, this.exercises, this.userId);
    }
}
