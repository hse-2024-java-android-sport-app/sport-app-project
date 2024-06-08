package org.sportApp.testData;

import androidx.annotation.NonNull;

import org.sportApp.dto.ExerciseDto;
import org.sportApp.dto.TrainingDto;

import java.util.ArrayList;
import java.util.List;

public class TestData {
    @NonNull
    public static List<ExerciseDto> getExercises() {
        List<ExerciseDto> exercises1 = new ArrayList<>();
        exercises1.add(new ExerciseDto("Squats", "Squats with dumbbells", 12, 30, 3));
        exercises1.add(new ExerciseDto("Dumbbell Bench Press", "Bench press with dumbbells on a flat bench", 10, 50, 3));
        exercises1.add(new ExerciseDto("Dumbbell Bent-Over Row", "Bent-over row with dumbbells", 12, 30, 3));
        exercises1.add(new ExerciseDto("Dumbbell Lateral Raise", "Lateral raises with dumbbells standing", 12, 30, 3));
        exercises1.add(new ExerciseDto("Barbell Bicep Curl", "Bicep curls with a barbell standing", 10, 60, 3));
        return exercises1;
    }

}
