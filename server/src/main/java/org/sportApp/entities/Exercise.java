package org.sportApp.entities;

import jakarta.persistence.*;

import java.time.Duration;
import java.util.Locale;

@Entity
@Table(name="Exercises")
public class Exercise {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String description;
    private int repetitions;
    private int duration;
    private int sets;
    private String videoUrl;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="trainId", nullable=false)
    private Training training;

    public Exercise(String description, int repetitions, int duration, int sets, String videoUrl, Training training) {
        this.description = description;
        this.repetitions = repetitions;
        this.duration = duration;
        this.sets = sets;
        this.videoUrl = videoUrl;
        this.training = training;
    }

    protected Exercise() {}

    public Long getId() {
        return this.id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public Training getTraining() {
        return training;
    }

    public void setTraining(Training training) {
        this.training = training;
    }

    @Override
    public String toString() {
        return String.format(Locale.GERMANY,
                "Exercise[id=%d, description='%s', repetitions='%d', duration='%s', sets='%d', videoUrl='%s', training='%s']",
                this.id, this.description, this.repetitions, this.duration, this.sets, this.videoUrl, this.training);
    }
}
