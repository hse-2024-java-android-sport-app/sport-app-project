package org.sportApp.entities;

import jakarta.persistence.*;
import org.sportApp.dto.TrainingDto;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

@Entity
@Table(name="TrainingEvents")
public class TrainingEvent {
    @Id
    private int eventId;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date date;
    private boolean completed;
    private String comment;
    @OneToOne(mappedBy = "event", cascade = CascadeType.ALL)
    private Training training;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="planId", nullable=false)
    private Plan plan;

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
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

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public Training getTraining() {
        return training;
    }

    public void setTraining(Training training) {
        this.training = training;
    }
}
