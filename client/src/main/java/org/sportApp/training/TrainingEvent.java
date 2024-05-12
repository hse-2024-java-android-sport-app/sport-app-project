package org.sportApp.training;

import org.sportApp.training.Training;

import java.time.LocalDate;
import java.util.Date;

public class TrainingEvent {
    private int id;
    private Date date;
    private boolean completed;
    private int userId;
    private String comment;
    private Training training;

    public TrainingEvent(){}
}
