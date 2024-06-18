package org.sportApp.entities;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name="TrainingEvents")
public class TrainingEvent {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int eventId;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date date;
    private boolean completed;
    private String comment;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="trainId")
    private Training training;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="planId", nullable=false)
    private Plan plan;

    public long getEventId() {
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

    @Override
    public String toString() {
        return "TrainingEvent{" +
                "eventId=" + eventId +
                ", date=" + date +
                ", completed=" + completed +
                ", comment='" + comment + '\'' +
                ", training=" + (training ==null) +
                ", planId=" + ((plan == null) ? null : plan.getPlanId()) +
                '}';
    }
}
