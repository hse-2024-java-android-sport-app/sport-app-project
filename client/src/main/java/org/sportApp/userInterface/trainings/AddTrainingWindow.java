package org.sportApp.userInterface.trainings;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.sportApp.requests.BackendService;
import org.sportApp.training.ExerciseDto;
import org.sportApp.training.TrainingDto;
import org.sportApp.training.TrainingEventDto;
import org.sportApp.userInterface.R;
import org.sportApp.utils.SessionManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.internal.concurrent.TaskRunner;

public class AddTrainingWindow extends AppCompatActivity {

    private LocalDate selectedDate;
    private ArrayList<ExerciseDto> exercises = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_training);

        Button selectDateButton = findViewById(R.id.datePickerButton);
        selectDateButton.setOnClickListener(v -> showDatePickerDialog());

        Button addExerciseButton = findViewById(R.id.addExerciseButton);
        addExerciseButton.setOnClickListener(v -> showExerciseSelectionDialog());

        Button saveTrainingButton = findViewById(R.id.saveTrainingButton);
        saveTrainingButton.setOnClickListener(v -> saveTrainingEvent());
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        selectedDate = LocalDate.of(year1, monthOfYear + 1, dayOfMonth);
                    }
                    Toast.makeText(AddTrainingWindow.this, "Date saved", Toast.LENGTH_SHORT).show();
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void showExerciseSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Exercise Option")
                .setItems(R.array.exercise_options, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            Intent intent = new Intent(AddTrainingWindow.this, AddExerciseWindow.class);
                            startActivity(intent);
                            ExerciseDto exerciseDto = getExerciseDto();
                            exercises.add(exerciseDto);
                            break;
                        case 1:
                            Toast.makeText(AddTrainingWindow.this, "Existing Exercise", Toast.LENGTH_SHORT).show();
                            break;
                    }
                });
        builder.create().show();
    }

    private void saveTrainingEvent() {
        if (selectedDate == null) {
            Toast.makeText(this, "You didn't select the date", Toast.LENGTH_SHORT).show();
            return;
        }

        TrainingDto trainingDto = new TrainingDto();
        trainingDto.setExercises(exercises);
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        long userId = sessionManager.getUserId();
        trainingDto.setUserId(userId);
        BackendService.addTraining(trainingDto);
        Toast.makeText(this, "Your training saved!", Toast.LENGTH_SHORT).show();
    }

    private ExerciseDto getExerciseDto() {
        Intent intent = getIntent();
        return intent.getParcelableExtra("exerciseDto");
    }
}
