package org.sportApp.userInterface.sportsman.ui.exercise;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.sportApp.requests.BackendService;
import org.sportApp.training.ExerciseDto;
import org.sportApp.userInterface.R;
import org.sportApp.userInterface.registration.TypeSelectionWindow;
import org.sportApp.utils.SessionManager;

import java.io.Serializable;

public class AddExerciseWindow extends AppCompatActivity {

    private EditText nameEditText;
    private EditText descriptionEditText;
    private EditText repetitionsEditText;
    private EditText durationEditText;
    private EditText setsEditText;
    private EditText videoUrlEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);
        nameEditText = findViewById(R.id.nameEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        repetitionsEditText = findViewById(R.id.repetitionsEditText);
        durationEditText = findViewById(R.id.durationEditText);
        setsEditText = findViewById(R.id.setsEditText);
        videoUrlEditText = findViewById(R.id.videoUrlEditText);

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> saveExercise());
    }

    private void saveExercise() {
        String name = nameEditText.getText().toString();
        String description = descriptionEditText.getText().toString();
        String repetitionsText = repetitionsEditText.getText().toString();
        String durationText = durationEditText.getText().toString();
        String setsText = setsEditText.getText().toString();
        String videoUrl = videoUrlEditText.getText().toString();

        if (description.isEmpty() || repetitionsText.isEmpty() || durationText.isEmpty() || setsText.isEmpty()) {
            Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int repetitions = Integer.parseInt(repetitionsText);
        int duration = Integer.parseInt(durationText);
        int sets = Integer.parseInt(setsText);

        ExerciseDto exerciseDto = new ExerciseDto();
        exerciseDto.setName(name);
        exerciseDto.setDescription(description);
        exerciseDto.setRepetitions(repetitions);
        exerciseDto.setDuration(duration);
        exerciseDto.setSets(sets);
        exerciseDto.setVideoUrl(videoUrl);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("exerciseDto",  exerciseDto);

        setResult(RESULT_OK, resultIntent);
        BackendService.addExercise(exerciseDto)
                .thenAccept(resultDto -> {
                    exerciseDto.setId(resultDto);

                    Log.d("id", resultDto.toString());
                    Toast.makeText(AddExerciseWindow.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                })
                .exceptionally(e -> {
                    Toast.makeText(AddExerciseWindow.this, "Registration failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return null;
                });
        finish();
    }
}