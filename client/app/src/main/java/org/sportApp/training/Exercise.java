package org.sportApp.training;

import java.time.Duration;

public class Exercise {
    private String description;
    private int repetitions;
    private Duration duration;
    private int sets;
    private String videoUrl;

    public Exercise(){}

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

    public void setDuration(Duration duration) {
        this.duration = duration;
    }


    public Duration getDuration() {
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
}
