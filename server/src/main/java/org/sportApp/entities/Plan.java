package org.sportApp.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="Plans")
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long planId;
    private String name;
    @OneToMany(mappedBy="plan", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TrainingEvent> trainings;
    private long coachId;
    private long sportsmanId;
    private LocalDate creationTime;

    protected Plan() {}

    public boolean isCompleted(){
        return trainings.stream().allMatch(TrainingEvent::isCompleted);
    }

    public long getPlanId() {
        return planId;
    }

    public void setPlanId(long planId) {
        this.planId = planId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCoachId() {
        return coachId;
    }

    public void setCoachId(long coachId) {
        this.coachId = coachId;
    }

    public long getSportsmanId() {
        return sportsmanId;
    }

    public void setSportsmanId(long sportsmanId) {
        this.sportsmanId = sportsmanId;
    }

    public List<TrainingEvent> getTrainings() {
        return trainings;
    }

    public void setTrainings(List<TrainingEvent> trainings) {
        this.trainings = trainings;
    }

    @Override
    public String toString() {
        return "Plan{" +
                "planId=" + planId +
                ", name=" + name +
                ", trainings=" + trainings +
                ", coachId='" + coachId +
                ", sportsmanId=" + (sportsmanId) +
                '}';
    }

    public LocalDate getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDate creationTime) {
        this.creationTime = creationTime;
    }
}
