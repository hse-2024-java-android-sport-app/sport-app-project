package org.sportApp.training;

import java.time.LocalDate;

public class TrainingEventDto {
    private long id;
    private LocalDate date;
    private boolean completed;
    private long userId;
    private String comment;
    private TrainingDto trainingDto;

    public TrainingEventDto(){}

    public TrainingDto getTrainingDto() {
        return trainingDto;
    }

    public void setDate(LocalDate selectedDate) {
    }

    public void setId(long i) {
        id = i;
    }

    public void setCompleted(boolean b) {
    }

    public long getId() {
        return id;
    }
}
