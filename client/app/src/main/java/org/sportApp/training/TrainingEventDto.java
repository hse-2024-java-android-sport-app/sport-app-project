package org.sportApp.training;

import java.time.LocalDate;

public class TrainingEventDto {
    private int id;
    private LocalDate date;
    private boolean completed;
    private int userId;
    private String comment;
    private TrainingDto trainingDto;

    public TrainingEventDto(){}

    public TrainingDto getTrainingDto() {
        return trainingDto;
    }
}
