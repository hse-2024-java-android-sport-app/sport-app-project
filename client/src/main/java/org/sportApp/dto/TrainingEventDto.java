package org.sportApp.training;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class TrainingEventDto implements Serializable {
    private long eventId;
    private Date date;
    private boolean completed;
    private String comment;
    private TrainingDto trainingDto;

    public TrainingEventDto(){}

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public long getEventId() {
        return eventId;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean getCompleted() {
        return completed;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }

    public void setTrainingDto(TrainingDto trainingDto) {
        this.trainingDto = trainingDto;
    }

    public TrainingDto getTrainingDto() {
        return trainingDto;
    }
}
