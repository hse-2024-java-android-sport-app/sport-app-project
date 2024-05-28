package org.sportApp.dto;

import java.util.Locale;

public class ExerciseDto {
    private String name;
    private String description;
    private int repetitions;
    private int duration;
    private int sets;
    private String videoUrl;
    private long trainId;

    public ExerciseDto(){}

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTrainId(long trainId) {
        this.trainId = trainId;
    }

    public long getTrainId() {
        return trainId;
    }

    @Override
    public String toString() {
        return String.format(Locale.GERMANY,
                "Exercise[description='%s', repetitions='%d', duration='%s', sets='%d', videoUrl='%s', trainId='%s']",
                this.description, this.repetitions, this.duration, this.sets, this.videoUrl, this.trainId);
    }
}
