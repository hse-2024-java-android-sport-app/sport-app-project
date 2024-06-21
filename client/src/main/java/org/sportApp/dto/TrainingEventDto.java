package org.sportApp.dto;

import java.io.Serializable;
import java.util.Date;

public class TrainingEventDto implements Serializable {
    private long eventId;
    //    @JsonFormat(pattern = "MMM dd, yyyy hh:mm:ss a")
    private Date date;
    private boolean completed;
    private TrainingDto training;

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

    public boolean isCompleted() {
        return completed;
    }

    public void setTraining(TrainingDto training) {
        this.training = training;
    }

    public TrainingDto getTraining() {
        return training;
    }
}
