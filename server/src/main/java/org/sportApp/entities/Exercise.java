package org.sportApp.entities;

import jakarta.persistence.*;

@Entity
@Table(name="Exercises")
public class Exercise {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;
    private int repetitions;
    private int duration;
    private int sets;
    private String videoUrl;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="trainId", nullable=false)
    private Training trainingDto;

    public Exercise(String name, String description, int repetitions, int duration, int sets, String videoUrl, Training training) {
        this.name = name;
        this.description = description;
        this.repetitions = repetitions;
        this.duration = duration;
        this.sets = sets;
        this.videoUrl = videoUrl;
        this.trainingDto = training;
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

    public Training getTrainingDto() {
        return trainingDto;
    }

    public void setTrainingDto(Training training) {
        this.trainingDto = training;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
