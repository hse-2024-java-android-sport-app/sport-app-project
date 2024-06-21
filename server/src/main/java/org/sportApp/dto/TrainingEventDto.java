package org.sportApp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class TrainingEventDto {
    private int eventId;
    @JsonFormat(pattern = "MMM dd, yyyy hh:mm:ss a")
    private Date date;
    private boolean completed;
    private TrainingDto training;

    protected TrainingEventDto() {
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int id) {
        this.eventId = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public TrainingDto getTraining() {
        return training;
    }

    public void setTraining(TrainingDto training) {
        this.training = training;
    }

    @Override
    public String toString() {
        return "TrainingEventDto{" +
                "eventId=" + eventId +
                ", date=" + date +
                ", completed=" + completed +
                ", training=" + training +
                '}';
    }
}

