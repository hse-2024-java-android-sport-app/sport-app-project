package org.sportApp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.Date;

public class TrainingEventDto {
    private int eventId;
    @JsonFormat(pattern = "MMM dd, yyyy hh:mm:ss a")
    private Date date;
    private boolean completed;
    private String comment;
    private TrainingDto training;

    public TrainingEventDto(int id, Date date, boolean completed, String comment, TrainingDto trainingDto){
        this.eventId = id;
        this.date = date;
        this.completed = completed;
        this.comment = comment;
        this.training = trainingDto;
    }

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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public TrainingDto getTraining() {
        return training;
    }

    public void setTraining(TrainingDto trainingDto) {
        this.training = trainingDto;
    }
}
