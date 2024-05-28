package org.sportApp.userInterface.sportsman.ui.trainingEvents;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import org.sportApp.requests.BackendService;
import org.sportApp.training.ExerciseDto;
import org.sportApp.training.TrainingDto;
import org.sportApp.training.TrainingEventDto;
import org.sportApp.userInterface.R;
import org.sportApp.userInterface.sportsman.ui.exercise.AddExerciseWindow;
import org.sportApp.userInterface.sportsman.ui.trainings.AddTrainingWindow;
import org.sportApp.userInterface.sportsman.ui.trainings.FindTraining;
import org.sportApp.utils.UserManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CreatingTypeSelectionWindow extends AppCompatActivity {
    private TrainingEventDto trainingEventDto;
    private Long planId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        trainingEventDto = new TrainingEventDto();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        Button createEvent = findViewById(R.id.createEvent);
        Button chooseEvent = findViewById(R.id.choseEvent);

        DatePicker datePicker = findViewById(R.id.datePicker);

        trainingEventDto.setDate(getSelectedDate(datePicker));

        createEvent.setOnClickListener(v -> {
            Log.d("myTag", "create Event");
            openAddTrainingWindow();
        });
        chooseEvent.setOnClickListener(v -> {
            Log.d("myTag", "choose Event");
            openAddTrainingWindow();
        });

        Button saveChanges = findViewById(R.id.saveChanges);
        saveChanges.setOnClickListener(v -> saveTrainingEvent());
    }
    @NonNull
    private Date getSelectedDate(@NonNull DatePicker datePicker) {
        int year = datePicker.getYear();
        int month = datePicker.getMonth() + 1;
        int day = datePicker.getDayOfMonth();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    private void openAddTrainingWindow() {
        Intent intent = new Intent(this, FindTraining.class);
        addTrainingLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> addTrainingLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        if (result.getResultCode() == RESULT_OK) {
            assert result.getData() != null;
            TrainingDto trainingDto = (TrainingDto) result.getData().getSerializableExtra("trainingDto");
            assert trainingDto != null;
            Log.d("myTag", "TrainingDto: " + trainingDto);
        }
    });

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            TrainingDto trainingDto = (TrainingDto) data.getSerializableExtra("trainingDto");
            if (trainingDto != null) {
                trainingEventDto.setTrainingDto(trainingDto);
            }
        }
    }

    private void saveTrainingEvent() {
        createTrainingEvent(trainingEventDto);
        Toast.makeText(this, "Your training event saved!", Toast.LENGTH_SHORT).show();
        Log.d("myTag", "user's id in training " + trainingEventDto);
    }

    private void createTrainingEvent(TrainingEventDto trainingEventDto) {
        BackendService.addEvent(trainingEventDto, planId).thenAccept(resultDto -> {
            trainingEventDto.setEventId(resultDto);
            Log.d("myTag", "training event's id: " + resultDto);
        }).exceptionally(e -> {
            Log.e("myTag", "Failed to create training or exercises.", e);
            return null;
        }).join();
    }
}
