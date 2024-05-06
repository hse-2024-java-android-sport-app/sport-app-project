package org.sportApp.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name="Plan")
public class Plan {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private long planId;
    private String name;
    @OneToMany(mappedBy="plan", cascade = CascadeType.ALL)
    private List<TrainingEvent> trainings;
    private long coachId;
    private long sportsmanId;

    protected Plan() {}

    private boolean isCompleted(){
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
}
