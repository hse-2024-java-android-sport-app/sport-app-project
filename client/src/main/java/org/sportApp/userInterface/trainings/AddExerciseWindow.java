package org.sportApp.userInterface.trainings;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.sportApp.requests.BackendService;
import org.sportApp.training.ExerciseDto;
import org.sportApp.userInterface.R;

import java.time.Duration;

public class AddExerciseWindow extends AppCompatActivity {

    private EditText descriptionEditText;
    private EditText repetitionsEditText;
    private EditText durationEditText;
    private EditText setsEditText;
    private EditText videoUrlEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        descriptionEditText = findViewById(R.id.descriptionEditText);
        repetitionsEditText = findViewById(R.id.repetitionsEditText);
        durationEditText = findViewById(R.id.durationEditText);
        setsEditText = findViewById(R.id.setsEditText);
        videoUrlEditText = findViewById(R.id.videoUrlEditText);

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> saveExercise());
    }

    private void saveExercise() {
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

        ExerciseDto exercise = new ExerciseDto();
        exercise.setDescription(description);
        exercise.setRepetitions(repetitions);
        exercise.setDuration(duration);
        exercise.setSets(sets);
        exercise.setVideoUrl(videoUrl);

        BackendService backendService = new BackendService();
        //backendService.saveExercise(exercise);

        finish();
    }
}