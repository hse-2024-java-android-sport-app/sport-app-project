package org.sportapp;

import org.sportapp.Training;

import java.time.LocalDate;

public class TrainingEvent {
    private int id;
    private LocalDate date;
    private boolean completed;
    private int userId;
    private String comment;
    private Training training;

    public TrainingEvent(){}
}