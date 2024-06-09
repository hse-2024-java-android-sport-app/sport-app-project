package org.sportApp.dto;

import java.io.Serializable;
public class ExerciseDto implements Serializable{
    private String name;
    private String description;
    private int repetitions;
    private int duration;
    private int sets;
    private String videoUrl;
    private Long id;

    public ExerciseDto(){}

    public ExerciseDto(String name, String description, int repetitions, int duration, int sets) {
        this.name = name;
        this.description = description;
        this.repetitions = repetitions;
        this.duration = duration;
        this.sets = sets;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setRepetitions(int repetitions) {
        this.repetitions = repetitions;
    }

    public int getRepetitions() {
        return repetitions;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }


    public int getDuration() {
        return duration;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getSets() {
        return sets;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId(){
        return id;
    }
}
