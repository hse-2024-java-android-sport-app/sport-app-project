package org.sportApp.userInterface.sportsman.ui.exercise;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.sportApp.dto.ExerciseDto;
import org.sportApp.userInterface.R;

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
        int repetitions, duration, sets;
        try {
            repetitions = Integer.parseInt(repetitionsText);
            duration = Integer.parseInt(durationText);
            sets = Integer.parseInt(setsText);
        }
        catch(NumberFormatException e){
            Toast.makeText(this, "Please enter a number greater than or equal to 1", Toast.LENGTH_SHORT).show();
            return;
        }

        if (repetitions * duration * sets == 0) {
            Toast.makeText(this, "Please enter a number greater than or equal to 1", Toast.LENGTH_SHORT).show();
            return;
        }

        ExerciseDto exerciseDto = new ExerciseDto(name, description, repetitions, duration, sets, videoUrl);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("exerciseDto",  exerciseDto);

        setResult(RESULT_OK, resultIntent);
        finish();
    }
}