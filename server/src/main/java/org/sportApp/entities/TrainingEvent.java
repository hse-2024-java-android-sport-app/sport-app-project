package org.sportApp.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Locale;

@Entity
@Table(name="TrainingEvents")
public class TrainingEvent {
    @Id
    private int id;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;
    private boolean completed;
    private int userId;
    private String comment;
    private long trainId;

    protected TrainingEvent() {}

    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getTrainId() {
        return trainId;
    }

    public void setTrainId(long trainId) {
        this.trainId = trainId;
    }

    public void setTrainId(Training training) {
        this.trainId = training.getTrainId();
    }

    @Override
    public String toString() {
        return String.format(Locale.GERMANY,
                "Training[id=%d, date='%s', completed='%b', userId='%d', comment='%s', trainId='%d']",
                this.trainId, this.date, this.completed, this.userId, this.comment, this.trainId);
    }
}
