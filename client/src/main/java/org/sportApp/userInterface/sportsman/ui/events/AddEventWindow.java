package org.sportApp.userInterface.sportsman.ui.trainingEvents;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.sportApp.requests.BackendService;
import org.sportApp.training.ExerciseDto;
import org.sportApp.training.TrainingDto;
import org.sportApp.userInterface.R;
import org.sportApp.userInterface.sportsman.ui.exercise.AddExerciseWindow;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class AddTrainingEventWindow extends AppCompatActivity {

    private LocalDate selectedDate;
    private final ArrayList<ExerciseDto> exercises = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        //Button selectDateButton = findViewById(R.id.datePickerButton);
        //selectDateButton.setOnClickListener(v -> showDatePickerDialog());

//        Button addExerciseButton = findViewById(R.id.addExerciseButton);
//        addExerciseButton.setOnClickListener(v -> showExerciseSelectionDialog());
//
//        Button saveTrainingButton = findViewById(R.id.saveTrainingButton);
//        saveTrainingButton.setOnClickListener(v -> saveTrainingEvent());
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, monthOfYear, dayOfMonth) -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                selectedDate = LocalDate.of(year1, monthOfYear + 1, dayOfMonth);
            }
            Toast.makeText(this, "Date saved", Toast.LENGTH_SHORT).show();
        }, year, month, day);
        datePickerDialog.show();
    }

    private final ActivityResultLauncher<Intent> addExerciseLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            assert result.getData() != null;
            ExerciseDto exerciseDto = (ExerciseDto) result.getData().getSerializableExtra("exerciseDto");
            assert exerciseDto != null;
            Log.d("exerciseDescription", exerciseDto.getDescription());
        }
    });

    private void showExerciseSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Exercise Option").setItems(R.array.exercise_options, (dialog, which) -> {
            switch (which) {
                case 0:
                    Intent intent = new Intent(this, AddExerciseWindow.class);
                    addExerciseLauncher.launch(intent);
                    break;
                case 1:
                    Toast.makeText(this, "Existing Exercise", Toast.LENGTH_SHORT).show();
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
        //trainingDto.setSportsmanId(userId);
        BackendService.createTraining(trainingDto);
        Toast.makeText(this, "Your training saved!", Toast.LENGTH_SHORT).show();
    }
}
